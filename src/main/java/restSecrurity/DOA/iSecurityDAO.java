package restSecrurity.DOA;

import restSecrurity.persistance.User;

public interface iSecurityDAO {
    public User verifyUser(String username, String password);
}
