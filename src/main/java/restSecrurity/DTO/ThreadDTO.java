package restSecrurity.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restSecrurity.persistance.*;
import restSecrurity.persistance.Thread;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ThreadDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private Set<PostDTO> posts;
    private String userName;
    private Set<String> roleNames;
    private String category;
    private int categoryId;

    public ThreadDTO(Thread thread) {
        User user = thread.getUser();
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        this.setId(thread.getId());
        this.setTitle(thread.getTitle());
        this.setContent(thread.getContent());
        this.setCreatedDate(thread.getCreatedDate());
        this.setUserName(user.getUsername());
        this.setRoleNames(roleNames);
        this.setCategory(thread.getCategory().getName());
    }
}

