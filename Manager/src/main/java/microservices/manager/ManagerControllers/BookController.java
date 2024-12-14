package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.BookClient;
import microservices.manager.dtos.BookDTO;
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
    public ResponseEntity<Optional<List<BookDTO>>> fetchAllBooks() {
        return bookClient.externalGetAllBooks();
    }

    @GetMapping("/fetchBookById/{id}")
    public ResponseEntity<Optional<BookDTO>> fetchBookById(@PathVariable Long id) {
        return bookClient.externalGetBookById(id);
    }


    @PostMapping("/saveBook")
    public ResponseEntity<Optional<BookDTO>> saveBook(@RequestBody BookDTO bookDTO) {
        return bookClient.externalSaveBook(bookDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Optional<BookDTO>> deleteBook(@PathVariable Long id){
        return bookClient.externalDeleteBook(id);
    }
}
