package restSecrurity.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restSecrurity.persistance.Post;
import restSecrurity.persistance.Reply;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    private Integer id;
    private String content;
    private LocalDateTime createdDate;
    private String userName;
    private Integer threadId;
    private Integer parentReplyId; // Updated to reflect parent reply

    private Set<ReplyDTO> replies = new HashSet<>();

    public PostDTO(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate();
        this.userName = post.getUser().getUsername();
        this.threadId = post.getThread().getId();

        for (Reply reply : post.getReplies()) {
            this.replies.add(new ReplyDTO(reply));
        }
    }

    public void addReply(ReplyDTO reply) {
        this.replies.add(reply);
    }
}
