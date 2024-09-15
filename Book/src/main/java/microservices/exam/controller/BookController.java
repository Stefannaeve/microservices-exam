package microservices.exam.controller;

import microservices.exam.apiResponse.ApiResponseBuilder;
import microservices.exam.models.Book;
import microservices.exam.service.BookService;
import microservices.exam.apiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/fetchAll")
    public ApiResponse fetchAll() {
        System.out.println("It works I think possibly maybe");
        ApiResponse books = bookService.fetchAll();



        return switch (books){
            case ApiResponse.Success success -> {
                for (int i = 0; i < success.value().get().size; i++) {
                    System.out.println(success.value().get().title);
                }
            }
            case ApiResponse.Failure failure -> System.out.println("Something went wrong: " + failure.errorMessage());
        };
        for (int i = 0; i < books.; i++) {
            System.out.println(books.Value.get().get(i).getTitle());
        };
        return books;
    }

    @PostMapping("/saveOneBook")
    public ApiResponse saveOneBook(@RequestBody Book book) {
        return bookService.saveOneBook(book);
    }


}
