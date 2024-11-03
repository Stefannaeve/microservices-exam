package microservices.user.repositories;

import microservices.user.models.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRepo extends JpaRepository<UserBook, Long> {

}
