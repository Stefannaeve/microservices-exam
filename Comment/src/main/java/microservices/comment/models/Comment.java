package microservices.comment.models;

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
public class Comment {

    public Comment(int page, String comment_text){
        this.page = page;
        this.comment_text = comment_text;
    }

    @Id
    @GeneratedValue(generator = "comment_id_generator")
    @SequenceGenerator(name = "comment_id_generator", sequenceName = "comment_id_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "page")
    private int page;

    @Lob
    @Column(name = "comment_text", columnDefinition = "VARCHAR(1000)")
    private String comment_text;
}
