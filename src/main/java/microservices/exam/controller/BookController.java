package microservices.exam.controller;

import microservices.exam.models.Book;
import microservices.exam.service.BookService;
import microservices.exam.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/fetchAll")
    public ApiResponse<List<Book>> fetchAll(){
        return bookService.fetchAll();
    }

    @PostMapping("/saveOneBook")
    public ApiResponse<Book> saveOneBook(@RequestBody Book book){
        return bookService.saveOneBook(book);
    }
}
