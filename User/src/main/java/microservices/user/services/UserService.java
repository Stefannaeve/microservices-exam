package microservices.user.services;

import lombok.extern.slf4j.Slf4j;
import microservices.user.apiResponse.ApiResponse;
import microservices.user.apiResponse.ApiResponseBuilder;
import microservices.user.eventDriven.UserEventPublisher;
import microservices.user.models.ReadingStatus;
import microservices.user.models.User;
import microservices.user.models.UserBook;
import microservices.user.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public ApiResponse<User> saveOneUser(User userToSave) {
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            User savedUser = userRepo.save(userToSave);
            userEventPublisher.publishCreateEvent(savedUser.getId());
            return apiResponseBuilder.success(savedUser);
        } catch (Exception e) {
            return new ApiResponse.Failure<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save user");
        }
    }

    public ApiResponse<User> fetchUserById(Long id){
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            User user = userRepo.findById(id).orElse(null);
            if (user != null){
                return apiResponseBuilder.success(user);
            }
            return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "No matching user found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<List<Long>> fetchUserBooks(Long userId) {
        ApiResponseBuilder<List<Long>> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            User user = userRepo.findById(userId).orElse(null);

            if (user == null) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "User not found");
            }
            else if (user.getBooks() == null || user.getBooks().isEmpty()) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "User has no books");
            }
            List<Long> bookId = new ArrayList<>();
            for (UserBook book : user.getBooks()) {
                bookId.add(book.getId());
            }
            return apiResponseBuilder.success(bookId);
        } catch (Exception e) {
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    public ApiResponse<User> addBookToUser(Long userId, UserBook userBook){
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();

        try {
            User user = userRepo.findById(userId).orElse(null);
            if (user == null){
                log.info(String.valueOf(user.getId()));
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "No matching user found");
            }
            user.getBooks().add(userBook);
            userRepo.save(user);
            return apiResponseBuilder.success(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<User> deleteUserById(Long userId) {
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            User user = userRepo.findById(userId).orElse(null);
            if (user == null) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "User not found");
            }
            userEventPublisher.publishDeleteEvent(userId);
            return apiResponseBuilder.success(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<User> deleteBookFromUser(Long userId, Long bookId) {
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();
        try {
            User user = userRepo.findById(userId).orElse(null);
            if (user == null) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "User not found");
            }
            Optional<UserBook> userBook = user.getBooks()
                    .stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst();

            if (userBook.isPresent()) {
                user.getBooks().remove(userBook.get());
                userRepo.save(user);
                userEventPublisher.publishBookDeletionEvent(userId, bookId);
                return apiResponseBuilder.success(user);
            } else {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "Book not found in user's book list");
            }
        } catch (Exception e) {
            return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "User has no books");
        }
    }

    public ApiResponse<User> updateReadingProgress(Long userId, Long bookId, String newProgress, String newStatus) {
        ApiResponseBuilder<User> apiResponseBuilder = new ApiResponseBuilder<>();

        try {
            User user = userRepo.findById(userId).orElse(null);
            if (user == null) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "User not found");
            }
            Optional<UserBook> userBook = user.getBooks()
                    .stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst();
            if (userBook.isEmpty()) {
                return apiResponseBuilder.failure(HttpStatus.NOT_FOUND, "Book not found in user's list");
            }

            ReadingStatus readingStatus;
            try {
                readingStatus = ReadingStatus.valueOf(newStatus);
            } catch (IllegalArgumentException e) {
                String validStatuses = Arrays.toString(ReadingStatus.values());
                return apiResponseBuilder.failure(HttpStatus.BAD_REQUEST,
                        "Not valid reading status. Valid options are: " + validStatuses);
            }
            UserBook book = userBook.get();
            book.setReadingProgress(newProgress);
            book.setReadingStatus(readingStatus);
            userRepo.save(user);

            userEventPublisher.publishProgressUpdateEvent(userId, bookId, newProgress, newStatus);
            return apiResponseBuilder.success(user);
        } catch (Exception e) {
            return apiResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Error, reading progress did not update");
        }
    }

}
