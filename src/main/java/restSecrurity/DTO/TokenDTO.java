package restSecrurity.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenDTO {
    String token;
    String username;
    String roles;

    public TokenDTO(String token, String username ){
        this.token = token;
        this.username = username;
    }
    public TokenDTO(String token, String username, String roles){
        this.token = token;
        this.username = username;
        this.roles = roles;
    }
}
