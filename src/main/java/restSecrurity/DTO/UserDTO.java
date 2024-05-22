package restSecrurity.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restSecrurity.persistance.User;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private Set<String> roles;
    private String email;

    public UserDTO(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRolesAsString();
        this.email = user.getEmail();
    }

    public UserDTO(String username, Set<String> rolesSet) {
        this.username = username;
        this.roles = rolesSet;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getRolesAsString() {
        return roles.stream().reduce((s1,s2) -> s1 +","+s2).get();
    }
}
