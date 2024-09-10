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
    public List<Book> fetchAll(){
        System.out.println("It works I think possibly maybe");
        ApiResponse<List<Book>> books = bookService.fetchAll();
        for (int i = 0; i < books.Value.get().size(); i++) {
            System.out.println(books.Value.get().get(i).getTitle());
        }
        return books.Value.get();
    }

    @PostMapping("/saveOneBook")
    public ApiResponse<Book> saveOneBook(@RequestBody Book book){
        return bookService.saveOneBook(book);
    }
}
