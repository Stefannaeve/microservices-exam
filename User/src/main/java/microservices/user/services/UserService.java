
package microservices.user.services;

import lombok.extern.slf4j.Slf4j;
import microservices.user.eventDriven.UserEventPublisher;
import microservices.user.models.ReadingStatus;
import microservices.user.models.User;
import microservices.user.models.UserBook;
import microservices.user.repositories.UserBookRepo;
import microservices.user.repositories.UserRepo;
import microservices.user.responseEntityInitializer.ResponseEntityInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepo userRepo;
    private final UserEventPublisher userEventPublisher;
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;
    private final UserBookRepo userBookRepo;

    @Autowired
    public UserService(UserRepo userRepo, UserEventPublisher userEventPublisher, DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration, UserBookRepo userBookRepo) {
        this.userRepo = userRepo;
        this.userEventPublisher = userEventPublisher;
        this.dataSourceTransactionManagerAutoConfiguration = dataSourceTransactionManagerAutoConfiguration;
        this.userBookRepo = userBookRepo;
    }

    public ResponseEntity<Optional<User>> saveOneUser(User userToSave) {
        Optional<User> savedUser = Optional.empty();
        try {
            savedUser = Optional.of(userRepo.save(userToSave));
            userEventPublisher.publishCreateEvent(savedUser.get().getId(), savedUser.get().getUsername());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    savedUser
            );
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage(), e);
        }
        return ResponseEntityInitializer.NewResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                false,
                "Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR,
                savedUser
        );
    }

    public ResponseEntity<Optional<User>> fetchUserById(Long id){
        Optional<User> user = Optional.empty();
        try {
            user = Optional.ofNullable(userRepo.findById(id).orElse(null));
            if (user.isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Could not find the user",
                        HttpStatus.NO_CONTENT,
                        user
                );
            }
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    user
            );
        } catch (Exception e) {
            log.error("Error trying to find user: {}", e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    user
            );
        }
    }

    public ResponseEntity<Optional<List<UserBook>>> fetchUserBooks(Long userId) {
        Optional<User> user = Optional.empty();
        try {
            user = userRepo.findById(userId);
            if (user.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Could not find the user",
                        HttpStatus.NO_CONTENT,
                        Optional.empty()
                );
            }
            else if (user.get().getBooks() == null || user.get().getBooks().isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "User has no books",
                        HttpStatus.NO_CONTENT,
                        Optional.empty()
                );
            }

            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    Optional.of(user.get().getBooks().stream().toList())
            );
        } catch (Exception e) {
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    Optional.empty()
            );
        }
    }

    public ResponseEntity<Optional<User>> addBookToUser(Long userId, UserBook userBook) {
        Optional<User> updatedUser = Optional.empty();
        Optional<User> databaseUser = Optional.empty();

        if (userBook.getRating() < 1 || userBook.getRating() > 10) {
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    false,
                    "Rating must be between 1 and 10",
                    HttpStatus.BAD_REQUEST,
                    updatedUser
            );
        }

        try {
            databaseUser = userRepo.findById(userId);
            if (databaseUser.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "No matching user found",
                        HttpStatus.NO_CONTENT,
                        updatedUser
                );
            }

            databaseUser.get().getBooks().add(userBook);
            updatedUser = Optional.of(userRepo.save(databaseUser.get()));
            userEventPublisher.publishAddBookEvent(userId, userBook);

            log.info("Added book to user with id: {} and queued the event", userId);
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    updatedUser
            );
        } catch (Exception e) {
            log.error("Error adding book to userId: {}", userId, e);
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    updatedUser
            );
        }
    }

    public ResponseEntity<Optional<User>> deleteUserById(Long userId) {
        Optional<User> user = Optional.empty();
        try {
            user = Optional.ofNullable(userRepo.findById(userId).orElse(null));
            if (user.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "User not found",
                        HttpStatus.NO_CONTENT,
                        user
                );
            }
            userEventPublisher.publishDeleteEvent(userId);
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    user
            );
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    user
            );
        }
    }

    public ResponseEntity<Optional<User>> deleteBookFromUser(Long userId, Long bookId) {
        Optional<User> user = Optional.empty();
        try {
            user = Optional.ofNullable(userRepo.findById(userId).orElse(null));
            if (user.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "User not found",
                        HttpStatus.NO_CONTENT,
                        user
                );
            }
            Optional<UserBook> userBook = user.get().getBooks()
                    .stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst();

            if (userBook.isPresent()) {
                user.get().getBooks().remove(userBook.get());
                userRepo.save(user.get());
                userEventPublisher.publishBookDeletionEvent(userId, bookId);
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        user
                );
            } else {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Book not found in user's book list",
                        HttpStatus.NO_CONTENT,
                        user
                );
            }
        } catch (Exception e) {
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    user
            );
        }
    }

    public ResponseEntity<Optional<User>> updateReadingProgressAndReadingStatus(Long userId, Long bookId, Map<String, String> requestBody) {
        Optional<User> user = Optional.empty();
        try {
            user = Optional.ofNullable(userRepo.findById(userId).orElse(null));
            if (user.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.NO_CONTENT,
                        false,
                        "User not found",
                        HttpStatus.NO_CONTENT,
                        user
                );
            }

            Optional<UserBook> userBook = user.get().getBooks()
                    .stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst();

            if (userBook.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.NO_CONTENT,
                        false,
                        "Book not found in user's list",
                        HttpStatus.NO_CONTENT,
                        user
                );
            }

            UserBook book = userBook.get();

            if (requestBody.containsKey("newReadingProgress")) {
                String newProgress = requestBody.get("newReadingProgress");
                book.setReadingProgress(newProgress);
            }

            if (requestBody.containsKey("newReadingStatus")) {
                String newStatus = requestBody.get("newReadingStatus");
                try {
                    ReadingStatus readingStatus = ReadingStatus.valueOf(newStatus);
                    book.setReadingStatus(readingStatus);
                } catch (IllegalArgumentException e) {
                    String validStatuses = Arrays.toString(ReadingStatus.values());
                    String errorMessage = "Not valid reading status. Valid options are: " + validStatuses;

                    return ResponseEntityInitializer.NewResponseEntity(
                            HttpStatus.BAD_REQUEST,
                            false,
                            errorMessage,
                            HttpStatus.BAD_REQUEST,
                            user
                    );
                }
            }

            userRepo.save(user.get());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    user
            );
        } catch (Exception e) {
            log.error("Error updating reading progress for userId: {} and bookId: {}", userId, bookId, e);
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    user
            );
        }
    }

    public ResponseEntity<Optional<User>> checkIfUserHasBook(Long userId, Long bookId) {
        Optional<User> user = Optional.empty();
        try {
            user = Optional.ofNullable(userRepo.findById(userId).orElse(null));
            if (user.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "User not found",
                        HttpStatus.NO_CONTENT,
                        user
                );
            }
            if (user.get().getBooks().stream().noneMatch(userBook -> Objects.equals(userBook.getId(), bookId))){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "User does not have that book",
                        HttpStatus.NO_CONTENT,
                        user
                );
            }
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    user
            );
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    user
            );
        }
    }

    public ResponseEntity<Optional<List<UserBook>>> fetchNotFinishedBooks(Long userId){
        Optional<User> findUser = Optional.empty();
        Optional<List<UserBook>> notFinishedBooks = Optional.empty();
        try{
            findUser = userRepo.findById(userId);
            if(findUser.isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "User not found",
                        HttpStatus.NO_CONTENT,
                        notFinishedBooks
                );
            }

            User user = findUser.get();
            notFinishedBooks = Optional.of(user.getBooks().stream()
                    .filter(book -> book.getReadingStatus() == ReadingStatus.DidNotFinish)
                    .collect(Collectors.toList()));

            if(notFinishedBooks.isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "There are no unfinished books for this user",
                        HttpStatus.NO_CONTENT,
                        notFinishedBooks
                );
            }
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    notFinishedBooks
            );
        } catch(Exception e){
            log.error("Error: {}", e.getMessage());
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    notFinishedBooks
            );
        }
    }
}

