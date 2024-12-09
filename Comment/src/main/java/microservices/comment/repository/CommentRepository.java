package microservices.comment.repository;

import microservices.comment.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserIdAndBookId(Long userId, Long bookId);
}

