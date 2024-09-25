package microservices.user.user.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName =
            "user_id_seq", initialValue = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("userIds")
    private List<BookId> books;
}
