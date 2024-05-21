package restSecrurity.DOA.databaseDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import restSecrurity.DTO.PostDTO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.persistance.Category;
import restSecrurity.persistance.Post;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostDAO extends DAO<Post,Integer> {

    private static PostDAO instance;

    public PostDAO(boolean isTest) {
        super(Post.class,false);
    }
    public static PostDAO getInstance(boolean isTest){
        if(instance == null){
            instance = new PostDAO(isTest);
        }
        return instance;
    }

    public Set<PostDTO> getAllPostsByThreadId(int id) throws ApiException {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p WHERE p.thread.id = :threadId", Post.class);
            query.setParameter("threadId", id);
            List<Post> posts = query.getResultList();
            Set<PostDTO> postDTOs = posts.stream().map(post -> new PostDTO(post)).collect(Collectors.toSet());
            return postDTOs;
        }catch (Exception e){
            throw new ApiException(500,"No posts was found");
        }
    }
}
