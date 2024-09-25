package microservices.user.user.repositories;

import microservices.user.user.models.BookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookIdRepo extends JpaRepository<BookId, Long> {

}
