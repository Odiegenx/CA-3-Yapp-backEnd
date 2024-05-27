package restSecrurity.DOA.databaseDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import restSecrurity.DOA.iSecurityDAO;
import restSecrurity.persistance.Role;
import restSecrurity.persistance.User;

public class UserDAO extends DAO<User,String> implements iSecurityDAO {

    private static UserDAO instance;

    public UserDAO(boolean isTest) {
        super(User.class,isTest);
    }

    public static UserDAO getInstance(boolean isTest){
        if(instance == null){
            instance = new UserDAO(isTest);
        }
        return instance;
    }

    public User verifyUser(String username,String password){
        EntityManager em = emf.createEntityManager();
        User userToFind = em.find(User.class,username);
        if(userToFind == null) throw new EntityNotFoundException("No user with username: " + username +" was found");
        if(!userToFind.verifyUser(password)){
            throw new EntityNotFoundException("Wrong password");
        }
        return userToFind;
    }
    @Override
    public User create(User user) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Role role = em.find(Role.class,"user");
            if(role == null){
                role = new Role("user");
                em.persist(role);
            }
            user.addRole(role);
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
    }

    public User getById(String id){
        User found;
        try(EntityManager em = emf.createEntityManager()){
            found = em.find(User.class, id);
            if(found == null){
                throw new EntityNotFoundException("No such user");
            }
        }
        return found;
    }
}
