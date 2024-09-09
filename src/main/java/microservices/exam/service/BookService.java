package microservices.exam.service;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import microservices.exam.utils.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static microservices.exam.utils.ApiResponse.Failure;
import static microservices.exam.utils.ApiResponse.Success;

@Service
@Slf4j
public class BookService {

    BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public ApiResponse fetchAll(){
        Optional<List<Book>> listBooks;
        try {
            listBooks = Optional.of(bookRepository.findAll());
        } catch (Exception exception){
            log.error(exception.getMessage());
            return Failure(exception.getMessage());
        }
        log.info("Returning list of books");
        return Success(listBooks);
    }

    public ApiResponse saveOneBook(Book book){
        try {
            Book bookFromDatabase = bookRepository.findBookByTitleAndAuthorAndPublishDate(book.getTitle(), book.getAuthor(), book.getPublishDate());
            if (bookFromDatabase == null){
                Optional<Book> savedBook = Optional.of(bookRepository.save(book));
                log.info("Added book to database");
                return Success(savedBook);
            } else {
                log.error("Tried adding already existing book");
                return Failure("Book already exists");
            }
        } catch (Exception exception){
            log.error(exception.getMessage());
            return Failure(exception.getMessage());
        }
    }


}
