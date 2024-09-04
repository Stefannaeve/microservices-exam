package microservices.exam.controller;

import microservices.exam.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

    BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }
}
