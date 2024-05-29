package restSecrurity.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restSecrurity.persistance.Post;
import restSecrurity.persistance.Reply;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class ReplyDTO {
    private Integer id;
    private String content;
    private LocalDateTime createdDate;
    private String userName; // Username of the user who created the reply
    private Integer parentPostId;

    public ReplyDTO(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.createdDate = reply.getCreatedDate();
        this.userName = reply.getUser().getUsername();
        this.parentPostId = reply.getParentPost().getId();
    }
}
