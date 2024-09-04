package microservices.exam.service;

import microservices.exam.models.Book;
import microservices.exam.repository.BookRepository;
import microservices.exam.utils.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
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

    public List<Book> fetchAll(){
        List<Book> listBooks;
        try {
            listBooks = bookRepository.findAll();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
        return listBooks;
    }

    public ApiResponse saveOneBook(Book book){
        try {
            // TODO:
            Book book1 = bookRepository.findBookByTitleAndAuthorAndPublishDate(book.getTitle(), book.getAuthor(), book.getPublishDate());
            //System.out.println("Hallo?" + book1.getTitle());
            if (book1 == null){
                Optional<Book> savedBook = Optional.of(bookRepository.save(book));
                return Success(savedBook);
            } else {
                System.out.println("Fuck off!");
                return Failure("Book already exists");
            }
        } catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }


}
