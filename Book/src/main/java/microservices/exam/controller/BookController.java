package microservices.exam.controller;

import lombok.extern.slf4j.Slf4j;
import microservices.exam.models.Book;
import microservices.exam.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/fetchBookById/{id}")
    public ResponseEntity<Optional<Book>> fetchBookById(@PathVariable Long id) {
        return bookService.fetchById(id);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<Optional<List<Book>>> fetchAll() {
        return bookService.fetchAll();
    }

    @PostMapping("/saveOneBook")
    public ResponseEntity<Optional<Book>> saveOneBook(@RequestBody Book book) {
        return bookService.saveOneBook(book);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<Optional<Book>> deleteBook(@PathVariable Long bookId) {
        return bookService.deleteBookById(bookId);
    }

    @GetMapping("/fetchByTitle/{title}")
    public ResponseEntity<Optional<List<Book>>> fetchBooksByTitle(@PathVariable String title) {
        return bookService.fetchBooksByTitle(title);
    }

    @GetMapping("/fetchByAuthor/{author}")
    public ResponseEntity<Optional<List<Book>>> fetchBooksByAuthor(@PathVariable String author) {
        return bookService.fetchBooksByAuthor(author);
    }
}