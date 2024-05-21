package restSecrurity.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restSecrurity.persistance.Post;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    private Integer id;
    private String content;
    private LocalDateTime createdDate;
    private String userName;
    private Integer threadId;
    private Integer replyId;
    private Integer parentReplyId;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate();
        this.userName = post.getUser().getUsername();
        this.threadId = post.getThread().getId();
        this.replyId = post.getReply() != null ? post.getReply().getId() : null;
        this.parentReplyId = post.getParentReply() != null ? post.getParentReply().getId() : null;
    }
}
