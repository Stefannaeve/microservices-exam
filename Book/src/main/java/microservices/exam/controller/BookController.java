package microservices.exam.controller;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.apiResponse.ResponseEntityInitializer;
import microservices.exam.clients.BookClient;
import microservices.exam.dtos.CommentDTO;
import microservices.exam.models.Book;
import microservices.exam.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Optional<Book>> fetchBookById(@PathVariable Long id) {
        ResponseEntity<Optional<Book>> response = bookService.fetchById(id);
        return response;
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<Optional<List<Book>>> fetchAll() {
        return bookService.fetchAll();
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
    public ResponseEntity<Optional<Book>> saveOneBook(@RequestBody Book book) {
        ResponseEntity<Optional<Book>> response = bookService.saveOneBook(book);
        return response;
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<Optional<Book>> deleteBook(@PathVariable Long bookId) {
        ResponseEntity<Optional<Book>> response = bookService.deleteBookById(bookId);
        return response;
    }

    @GetMapping("/fetchByTitle/{title}")
    public ResponseEntity<Optional<List<Book>>> fetchBooksByTitle(@PathVariable String title) {
        ResponseEntity<Optional<List<Book>>> response = bookService.fetchBooksByTitle(title);
        return response;
    }
}