package microservices.manager.ManagerControllers;

import lombok.extern.slf4j.Slf4j;
import microservices.manager.ManagerClients.BookClient;
import microservices.manager.apiResponse.ApiResponse;
import microservices.manager.dtos.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manager/book")
public class BookController {

    private final BookClient bookClient;

    public BookController(BookClient bookClient) {
        this.bookClient = bookClient;
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<BookDTO>>> fetchAllBooks() {
        ApiResponse<List<BookDTO>> books = bookClient.externalGetAllBooks();

        switch (books) {
            case ApiResponse.Success<List<BookDTO>> success -> {
                if (success.value().isPresent()) {
                    log.info("Success, returning {} books from book service", success.value().get().size());
                } else {
                    log.info("Success");
                }
                return ResponseEntity.status(200).body(success);
            }
            case ApiResponse.Failure<List<BookDTO>> failure -> {
                log.error(failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(failure);
            }
        }
    }

    @GetMapping("/fetchBookById/{id}")
    public ResponseEntity<ApiResponse<BookDTO>> fetchBookById(@PathVariable long id) {
        ApiResponse<BookDTO> book = bookClient.externalGetBookById(id);

        switch (book) {
            case ApiResponse.Success<BookDTO> success -> {
                return ResponseEntity.status(HttpStatus.OK).body(success);
            }
            case ApiResponse.Failure<BookDTO> failure -> {
                log.error("This is the error:", failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(failure);
            }
        }
    }


    @PostMapping("/saveBook")
    public ResponseEntity<ApiResponse<BookDTO>> saveBook(@RequestBody BookDTO bookDTO) {
        ApiResponse<BookDTO> savedBook = bookClient.externalSaveBook(bookDTO);

        switch (savedBook) {
            case ApiResponse.Success<BookDTO> success -> {
                if (success.value().isPresent()) {
                    log.info("Success, saved book with id: {}", success.value().get().getId());
                    bookClient.externalSaveBook(bookDTO);
                } else {
                    log.info("Success");
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(success);
            }
            case ApiResponse.Failure<BookDTO> failure -> {
                log.error("This is the error:  {}", failure.errorMessage());
                return ResponseEntity.status(failure.status()).body(failure);
            }
        }
    }
}
