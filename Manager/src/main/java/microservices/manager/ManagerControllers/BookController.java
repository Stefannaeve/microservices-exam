package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.BookClient;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.dtos.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/manager/book")
public class BookController {

    private final BookClient bookClient;

    public BookController(BookClient bookClient) {
        this.bookClient = bookClient;
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<BookDTO>> fetchAllBooks() {
        return bookClient.externalGetAllBooks();
    }

    @GetMapping("/fetchBookById/{id}")
    public ResponseEntity<Optional<BookDTO>> fetchBookById(@PathVariable long id) {
        ResponseEntity<Optional<BookDTO>> book = bookClient.externalGetBookById(id);
        return book;
    }


    @PostMapping("/saveBook")
    public ResponseEntity<Optional<BookDTO>> saveBook(@RequestBody BookDTO bookDTO) {
        ResponseEntity<Optional<BookDTO>> savedBook = bookClient.externalSaveBook(bookDTO);
        return savedBook;
    }
}
