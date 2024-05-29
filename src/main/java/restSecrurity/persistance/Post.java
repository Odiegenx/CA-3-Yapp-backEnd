package restSecrurity.persistance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Post.java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "thread_id", nullable = false)
    private Thread thread;

    @OneToMany(mappedBy = "parentPost", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<Reply> replies = new HashSet<>();

    public Post(String content, User user, Thread thread) {
        this.content = content;
        this.user = user;
        this.thread = thread;
        this.createdDate = LocalDateTime.now();
    }

    public Post(String content, User user, Thread thread, Post parentPost) {
        this(content, user, thread);
        if (parentPost != null) {
            parentPost.addReply(new Reply(content, parentPost, user));
        }
    }

    public void addReply(Reply reply) {
        if (reply != null) {
            if (this.replies == null) {
                this.replies = new HashSet<>();
            }
            this.replies.add(reply);
            reply.setParentPost(this);
        }
    }
}





