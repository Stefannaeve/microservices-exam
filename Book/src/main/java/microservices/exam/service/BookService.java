package microservices.exam.service;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponseBuilder;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            return ApiResponseBuilder.Failure(exception.getMessage());
        }
        log.info("Returning list of books");

        System.out.println();

        return ApiResponseBuilder.Success(listBooks);
        //return new ApiResponse.Success(listBooks);
    }

    public ApiResponse saveOneBook(Book book){
        try {
            Book bookFromDatabase = bookRepository.findBookByTitleAndAuthorAndPublishDate(book.getTitle(), book.getAuthor(), book.getPublishDate());
            if (bookFromDatabase == null){
                Optional<Book> savedBook = Optional.of(bookRepository.save(book));
                log.info("Added book to database");
                ApiResponse apiResponse = ApiResponseBuilder.Failure("Error");
                return apiResponse;
            } else {
                log.error("Tried adding already existing book");
                return ApiResponseBuilder.Failure("Book already exists");
            }
        } catch (Exception exception){
            log.error(exception.getMessage());
            return ApiResponseBuilder.Failure(exception.getMessage());
        }
    }


}
