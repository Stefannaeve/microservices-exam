package microservices.user.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userBook")
public class UserBook {

    @Id
    private Long id;

    @Column(name = "pages")
    private int pages;

    @Column(name = "readingProgress")
    private String readingProgress;

    @Enumerated(EnumType.STRING)
    @Column(name = "readingStatus")
    private ReadingStatus readingStatus;
}
