package microservices.exam.controller;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.clients.BookClient;
import microservices.exam.dtos.CommentDTO;
import microservices.exam.models.Book;
import microservices.exam.service.BookService;
import microservices.exam.apiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    BookService bookService;
    BookClient bookClient;

    @Autowired
    public BookController(BookService bookService, BookClient bookClient) {
        this.bookService = bookService;
        this.bookClient = bookClient;
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<Book>>> fetchAll() {
        ApiResponse<List<Book>> books = bookService.fetchAll();

        switch (books) {
            case ApiResponse.Success<List<Book>> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(books);
            }
            case ApiResponse.Failure<List<Book>> failure -> {
                log.error(failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(books);
            }
        }
    }

    @GetMapping("/fetchAllComments")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> fetchAllComments(){
        ApiResponse<List<CommentDTO>> comments = bookClient.externalComment();
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

//    @GetMapping("/fetchAllComments")
//    public ResponseEntity<List<CommentDTO>> fetchAllComments(){
//        List<CommentDTO> comments = bookClient.externalComment();
//        return ResponseEntity.status(HttpStatus.OK).body(comments);
//    }

    /*
    @GetMapping("/fetchAll")
    public ResponseEntity<List<Book>> fetchAll() {
        List<Book> books = bookService.fetchAll();

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }
     */

    @PostMapping("/saveOneBook")
    public ResponseEntity<ApiResponse<Book>> saveOneBook(@RequestBody Book book) {
        ApiResponse<Book> savedBook = bookService.saveOneBook(book);

        switch (savedBook) {
            case ApiResponse.Success<Book> success -> {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
            }
            case ApiResponse.Failure<Book> failure -> {
                System.out.println("Something went wrong: " + failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(savedBook);
            }
        }
    }


}
