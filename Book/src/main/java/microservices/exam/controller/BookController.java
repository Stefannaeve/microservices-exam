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

    BookService bookService;
    BookClient bookClient;

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
                return ResponseEntity.status(failure.status()).body(failure);
            }
        }
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<Book>>> fetchAll() {
        ApiResponse<List<Book>> apiResponse = bookService.fetchAll();

        switch (apiResponse) {
            case ApiResponse.Success<List<Book>> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<List<Book>> failure -> {
                log.error(failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(failure);
            }
        }
    }

    @GetMapping("/fetchAllComments")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> fetchAllComments() {
        ApiResponse<List<CommentDTO>> comments = bookClient.externalComment();
        return ResponseEntity.status(comments.status()).body(comments);
    }

    @PostMapping("/saveOneBook")
    public ResponseEntity<ApiResponse<Book>> saveOneBook(@RequestBody Book book) {
        ApiResponse<Book> savedBook = bookService.saveOneBook(book);
        return ResponseEntity.status(savedBook.status()).body(savedBook);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long bookId) {
        ApiResponse<Void> response = bookService.deleteBookById(bookId);
        return ResponseEntity.status(response.status()).body(response);
    }
}
