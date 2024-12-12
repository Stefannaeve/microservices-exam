package microservices.exam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.clients.BookClient;
import microservices.exam.dtos.CommentDTO;
import microservices.exam.models.Book;
import microservices.exam.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final BookClient bookClient;

    @Autowired
    public BookController(BookService bookService, BookClient bookClient) {
        this.bookService = bookService;
        this.bookClient = bookClient;
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<Book>>> fetchAll() {
        ApiResponse<List<Book>> apiResponse = bookService.fetchAll();

        switch (apiResponse) {
            case ApiResponse.Success<List<Book>> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<Book>> failure -> {
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/fetchAllComments")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> fetchAllComments() {
        ApiResponse<List<CommentDTO>> externalComment = bookClient.externalComment();

        switch (externalComment){
            case ApiResponse.Success<List<CommentDTO>> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<CommentDTO>> failure -> {
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @PostMapping("/saveOneBook")
    public ResponseEntity<ApiResponse<Book>> saveOneBook(@RequestBody Book book) {
        ApiResponse<Book> apiResponse = bookService.saveOneBook(book);

        switch (apiResponse){
            case ApiResponse.Success<Book> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<Book> failure -> {
                return new ResponseEntity<>(failure, failure.status());
            }
        }

    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long bookId) {
        ApiResponse<Void> apiResponse = bookService.deleteBookById(bookId);

        switch (apiResponse){
            case ApiResponse.Success<Void> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<Void> failure -> {
                return new ResponseEntity<>(failure, failure.status());
            }
        }

    }
}
