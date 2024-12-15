
package microservices.user.services;

import lombok.extern.slf4j.Slf4j;
import microservices.user.apiResponse.ResponseEntityInitializer;
import microservices.user.eventDriven.UserEventPublisher;
import microservices.user.models.ReadingStatus;
import microservices.user.models.User;
import microservices.user.models.UserBook;
import microservices.user.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserRepo userRepo;
    private final UserEventPublisher userEventPublisher;

    @Autowired
    public UserService(UserRepo userRepo, UserEventPublisher userEventPublisher) {
        this.userRepo = userRepo;
        this.userEventPublisher = userEventPublisher;
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
                        HttpStatus.NOT_FOUND,
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

    public ResponseEntity<Optional<List<Long>>> fetchUserBooks(Long userId) {
        Optional<User> user = Optional.empty();
        Optional<List<Long>> bookId = Optional.empty();
        try {
            user = userRepo.findById(userId);

            if (user.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "Could not find the user",
                        HttpStatus.NOT_FOUND,
                        bookId
                );
            }
            else if (user.get().getBooks() == null || user.get().getBooks().isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "User has no books",
                        HttpStatus.NOT_FOUND,
                        bookId
                );
            }
            List<Long> bookIds = new ArrayList<>();
            for (UserBook book : user.get().getBooks()) {
                bookIds.add(book.getId());
            }
            bookId = Optional.of(bookIds);
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    bookId
            );
        } catch (Exception e) {
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    "An unknown error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    bookId
            );
        }
    }

    public ResponseEntity<Optional<User>> addBookToUser(Long userId, UserBook userBook) {
        Optional<Integer> rating = Optional.empty();
        Optional<User> user = Optional.empty();
        Optional<User> updatedUser = Optional.empty();
        Optional<User> userOptional = Optional.empty();

        rating = Optional.of(userBook.getRating());
        if (rating.isEmpty() && (rating.get() < 1 || rating.get() > 10)) {
            return ResponseEntityInitializer.NewResponseEntity(
                    HttpStatus.OK,
                    false,
                    "Rating must be between 1 and 10",
                    HttpStatus.BAD_REQUEST,
                    updatedUser
            );
        }

        try {
            userOptional = userRepo.findById(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "No matching user found",
                        HttpStatus.NOT_FOUND,
                        updatedUser
                );
            }

            user = Optional.of(userOptional.get());
            user.get().getBooks().add(userBook);
            updatedUser = Optional.of(userRepo.save(user.get()));
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
                        HttpStatus.NOT_FOUND,
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
                        HttpStatus.NOT_FOUND,
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
                        HttpStatus.NOT_FOUND,
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

    public ResponseEntity<Optional<User>> updateReadingProgress(Long userId, Long bookId, Map<String, String> requestBody) {
        Optional<User> user = Optional.empty();
        try {
            user = Optional.ofNullable(userRepo.findById(userId).orElse(null));
            if (user.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.NOT_FOUND,
                        false,
                        "User not found",
                        HttpStatus.NOT_FOUND,
                        user
                );
            }

            Optional<UserBook> userBook = user.get().getBooks()
                    .stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst();

            if (userBook.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.NOT_FOUND,
                        false,
                        "Book not found in user's list",
                        HttpStatus.NOT_FOUND,
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

    public ResponseEntity<Optional<User>> fetchUserWithBook(Long userId, Long bookId) {
        Optional<User> user = Optional.empty();
        try {
            user = Optional.ofNullable(userRepo.findById(userId).orElse(null));
            if (user.isEmpty()) {
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "User not found",
                        HttpStatus.NOT_FOUND,
                        user
                );
            }
            if (user.get().getBooks().stream().noneMatch(userBook -> userBook.getId() == bookId)){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "User does not have that book",
                        HttpStatus.NOT_FOUND,
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
                        HttpStatus.NOT_FOUND,
                        notFinishedBooks
                );
            }

            User user = findUser.get();
            notFinishedBooks = Optional.of(user.getBooks().stream().filter(book -> book.getReadingStatus() == ReadingStatus.DidNotFinish).toList());

            if(notFinishedBooks.isEmpty()){
                return ResponseEntityInitializer.NewResponseEntity(
                        HttpStatus.OK,
                        false,
                        "There are no unfinished books for this user",
                        HttpStatus.NOT_FOUND,
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

