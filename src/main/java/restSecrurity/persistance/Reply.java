package restSecrurity.persistance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Reply.java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "replies")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_post_id", nullable = false)
    private Post parentPost;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Reply(String content, Post parentPost, User user) {
        this.content = content;
        this.parentPost = parentPost;
        this.user = user;
        this.createdDate = LocalDateTime.now();
    }
}





