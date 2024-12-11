package microservices.exam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.exam.apiResponse.ApiResponse;
import microservices.exam.clients.BookClient;
import microservices.exam.dtos.CommentDTO;
import microservices.exam.models.Book;
import microservices.exam.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookClient bookClient;

    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<Book>>> fetchAll() {
        ApiResponse<List<Book>> apiResponse = bookService.fetchAll();

        switch (apiResponse) {
            case ApiResponse.Success<List<Book>> success -> {

            }
        }
        return ResponseEntity.status(books.status()).body(books);
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
