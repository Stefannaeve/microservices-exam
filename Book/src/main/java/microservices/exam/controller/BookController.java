package microservices.exam.controller;

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

    @GetMapping("/fetchBookById/{id}")
    public ResponseEntity<ApiResponse<Book>> fetchBookById(@PathVariable Long id) {
        ApiResponse<Book> book = bookService.fetchById(id);

        switch (book) {
            case ApiResponse.Success<Book> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<Book> failure -> {
                log.error(failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<Book>>> fetchAll() {
        ApiResponse<List<Book>> apiResponse = bookService.fetchAll();

        switch (apiResponse) {
            case ApiResponse.Success<List<Book>> success -> {
                log.info("Fetched all books successfully.");
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<Book>> failure -> {
                log.error("Failed to fetch books: {}", failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @GetMapping("/fetchAllComments")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> fetchAllComments() {
        ApiResponse<List<CommentDTO>> externalComment = bookClient.externalComment();

        switch (externalComment) {
            case ApiResponse.Success<List<CommentDTO>> success -> {
                if (success.value().isPresent()) {
                    log.info("Fetched all comments successfully. Total comments: {}", success.value().get().size());
                } else {
                    log.info("Fetched all comments successfully, but no comments were found.");
                }
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<CommentDTO>> failure -> {
                log.error("Failed to fetch comments: {}", failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @PostMapping("/saveOneBook")
    public ResponseEntity<ApiResponse<Book>> saveOneBook(@RequestBody Book book) {
        ApiResponse<Book> apiResponse = bookService.saveOneBook(book);

        switch (apiResponse) {
            case ApiResponse.Success<Book> success -> {
                if (success.value().isPresent()) {
                    log.info("Saved book with id: {}", success.value().get().getId());
                } else {
                    log.info("Book saved successfully.");
                }
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<Book> failure -> {
                log.error("Failed to save book: {}", failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long bookId) {
        ApiResponse<Void> apiResponse = bookService.deleteBookById(bookId);

        switch (apiResponse) {
            case ApiResponse.Success<Void> success -> {
                log.info("Deleted book with id: {}", bookId);
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<Void> failure -> {
                log.error("Failed to delete book with id: {}: {}", bookId, failure.errorMessage());
                return new ResponseEntity<>(failure, failure.status());
            }
        }
    }
}