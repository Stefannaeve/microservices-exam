package microservices.exam.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import microservices.exam.responseEntityInitializer.ResponseEntityInitializer;
import microservices.exam.eventDriven.BookEventPublisher;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.InputStreamReader;
import java.util.*;

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final BookEventPublisher bookEventPublisher;

    public BookService(BookRepository bookRepository, BookEventPublisher bookEventPublisher) {
        this.bookRepository = bookRepository;
        this.bookEventPublisher = bookEventPublisher;
    }

    public ResponseEntity<Optional<List<Book>>> fetchAll() {
        Optional<List<Book>> books = Optional.empty();

        try {
            books = Optional.of(bookRepository.findAll());
            if (books.get().size() <= 0){
                log.info("Found no books");
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Found no books",
                        HttpStatus.NO_CONTENT,
                        books
                );
            }
            log.info("Found {} books", books.get().size());

            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    books
            );
        } catch (Exception exception) {
            log.error("Error fetching books: {}", exception.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    books
            );
        }
    }

    public ResponseEntity<Optional<Book>> saveOneBook(Book book) {
        Optional<Book> savedBook = Optional.empty();
        try {

            Book bookFromDatabase = bookRepository.findBookByTitleAndAuthorAndPublishDate(book.getTitle(), book.getAuthor(), book.getPublishDate());
            if (bookFromDatabase == null) {
                savedBook = Optional.of(bookRepository.save(book));
                if (savedBook.isEmpty()) {
                    return ResponseEntityInitializer.NewResponseEntity(
                            HttpStatus.OK,
                            false,
                            "Failed to save book",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            savedBook
                    );
                }
                log.info("Saved book with id: {}", savedBook.get().getId());

                bookEventPublisher.publishBookCreatedEvent(
                        savedBook.get().getId(),
                        savedBook.get().getTitle(),
                        savedBook.get().getAuthor(),
                        savedBook.get().getPages(),
                        savedBook.get().getPublishDate(),
                        savedBook.get().getBookContent()
                );

                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.CREATED,
                        savedBook
                );
            } else {
                log.info("Book from database: {}", bookFromDatabase.getTitle());
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Book already exists",
                        HttpStatus.CONFLICT,
                        savedBook
                );
            }
        } catch (Exception e) {
            log.error("Error saving book: {}", e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    savedBook
            );
        }
    }

    public ResponseEntity<Optional<Book>> deleteBookById(Long bookId) {
        Optional<Book> book = Optional.empty();
        try {
            book = bookRepository.findById(bookId);
            if (book.isPresent()) {
                bookRepository.delete(book.get());
                log.info("Deleted book with id: {}", bookId);

                bookEventPublisher.publishBookDeletedEvent(bookId);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        book
                );
            } else {
                log.warn("Book with id {} not found for deletion", bookId);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Book not found for deletion",
                        HttpStatus.NO_CONTENT,
                        book
                );
            }
        } catch (Exception e) {
            log.error("Error deleting book with id {}: {}", bookId, e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    book
            );
        }
    }

    public ResponseEntity<Optional<Book>> fetchById(long id) {
        Optional<Book> book = Optional.empty();
        try {
            book = bookRepository.findById(id);
            if (book.isPresent()) {
                log.info("Book found with id: {}", id);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        book
                );
            } else {
                log.warn("Book with id {} not found", id);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Book not found",
                        HttpStatus.NO_CONTENT,
                        book
                );
            }
        } catch (Exception exception) {
            log.error("Error fetching book by id {}: {}", id, exception.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    book
            );

        }
    }

    public ResponseEntity<Optional<List<Book>>> fetchBooksByTitle(String title) {
        Optional<List<Book>> books = Optional.empty();
        try {
            books = Optional.ofNullable(bookRepository.findByTitle(title));
            if (books.isPresent() && !books.get().isEmpty()) {
                log.info("Found {} books with title containing: {}", books.get().size(), title);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        books
                );
            } else {
                log.warn("No books found with title containing: {}", title);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "No books found with the specified title",
                        HttpStatus.NO_CONTENT,
                        books
                );
            }
        } catch (Exception e) {
            log.error("Error fetching books by title {}: {}", title, e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    books
            );

        }
    }

    public ResponseEntity<Optional<List<Book>>> fetchBooksByAuthor(String author) {
        Optional<List<Book>> books = Optional.empty();
        try {
            books = Optional.ofNullable(bookRepository.findByAuthor(author));
            if (books.isPresent() && !books.get().isEmpty()) {
                log.info("Found {} books with author containing: {}", books.get().size(), author);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        books
                );
            } else {
                log.warn("No books found with author containing: {}", author);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "No books found with the specified author",
                        HttpStatus.NO_CONTENT,
                        books
                );
            }
        } catch (Exception e) {
            log.error("Error fetching books by author {}: {}", author, e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    books
            );
        }
    }

    public ResponseEntity<Optional<List<Book>>> populateDatabaseFromGutenberg(int maxBookCount) {
        RestTemplate restTemplate = new RestTemplate();
        Optional<List<Book>> books = Optional.empty();
        try {
            books = Optional.ofNullable(restTemplate.execute(
                    "https://www.gutenberg.org/cache/epub/feeds/pg_catalog.csv",
                    HttpMethod.GET,
                    null,
                    clientHttpResponse -> {
                        InputStreamReader reader = new InputStreamReader(clientHttpResponse.getBody());
                        CsvToBean<Book> csvToBean = new CsvToBeanBuilder<Book>(reader)
                                .withType(Book.class)
                                .withSeparator(',')
                                .build();

                        List<Book> bookList = new ArrayList<>();
                        Iterator<Book> iterator = csvToBean.iterator();
                        int i = 0;

                        while (iterator.hasNext() == true && i < maxBookCount) {
                            bookList.add(iterator.next());
                            iterator.next();
                            i++;
                        }

                        for (Book book : bookList) {
                            bookRepository.save(book);
                        }
                        return bookList;
                    }
            ));
        } catch (RestClientException clientException) {
            log.error("Encountered error while connecting to external service");
            log.error(clientException.getMessage());
            clientException.printStackTrace();

            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    false,
                    "Encountered error while connecting to external service",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    books
            );
        } catch (ClassCastException | NullPointerException | NoSuchElementException parsingException) {
            log.error("Encountered error while parsing response from external service");
            log.error(parsingException.getMessage());
            parsingException.printStackTrace();

            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    false,
                    "Encountered error while parsing response from external service",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    books
            );
        } catch (Exception exception) {
            log.error("Unexpected error occurred");
            log.error(exception.getMessage());
            exception.printStackTrace();

            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    false,
                    "Unexpected error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    books
            );
        }

        return ResponseEntityInitializer.NewResponseEntity(
                HttpStatus.OK,
                books
        );
    }

    public ResponseEntity<Optional<Book>> fetchBookContentFromGutenberg(Long bookId) {
        CloseableHttpClient
                httpClient = HttpClients.custom()
                .setRedirectStrategy(new DefaultRedirectStrategy())
                .build();

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        Optional<Book> book = Optional.empty();

        book = bookRepository.findById(bookId);
        if (book == null){
            log.warn("Book with id {} not found", bookId);
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    false,
                    "Book not found",
                    HttpStatus.NO_CONTENT,
                    book
            );
        }

        try {

           if (book.get().getBookContent() != null){
               return ResponseEntityInitializer.NewResponseEntity(
                       HttpStatus.OK,
                       book
               );
           }

            String url = String.format("https://gutenberg.org/ebooks/%d.txt.utf-8", bookId.intValue());
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK){
                book.get().setBookContent(response.getBody());
                bookRepository.save(book.get());
            }
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    book
            );

        } catch (RestClientException clientException) {
            log.error("Encountered error while connecting to external service");
            log.error(clientException.getMessage());
            clientException.printStackTrace();

            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    false,
                    "Encountered error while connecting to external service",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    book
            );
        } catch (Exception exception) {
            log.error("Unexpected error occurred");
            log.error(exception.getMessage());
            exception.printStackTrace();

            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    false,
                    "Unexpected error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    book
            );
        }
    }
}