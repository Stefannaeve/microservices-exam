package microservices.exam.service;

import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import microservices.exam.utils.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static microservices.exam.utils.ApiResponse.Failure;
import static microservices.exam.utils.ApiResponse.Success;

@Service
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
            System.out.println(exception.getMessage());
            return Failure(exception.getMessage());
        }
        return Success(listBooks);
    }

    public ApiResponse saveOneBook(Book book){
        try {
            Book bookFromDatabase = bookRepository.findBookByTitleAndAuthorAndPublishDate(book.getTitle(), book.getAuthor(), book.getPublishDate());
            if (bookFromDatabase == null){
                Optional<Book> savedBook = Optional.of(bookRepository.save(book));
                return Success(savedBook);
            } else {
                return Failure("Book already exists");
            }
        } catch (Exception exception){
            return Failure(exception.getMessage());
        }
    }


}
