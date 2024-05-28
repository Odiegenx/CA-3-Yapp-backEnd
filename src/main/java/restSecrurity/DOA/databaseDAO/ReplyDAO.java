package restSecrurity.DOA.databaseDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import restSecrurity.DTO.PostDTO;
import restSecrurity.DTO.ReplyDTO;
import restSecrurity.persistance.Reply;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReplyDAO extends DAO<Reply,Integer> {

    private static ReplyDAO instance;

    public ReplyDAO(boolean isTest) {
        super(Reply.class,false);
    }
    public static ReplyDAO getInstance(boolean isTest){
        if(instance == null){
            instance = new ReplyDAO(isTest);
        }
        return instance;
    }

    public static Set<ReplyDTO> getAllRepliesByPostId(int postId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Reply> query = em.createQuery(
                    "SELECT r FROM Reply r WHERE r.parentPost.id = :postId",
                    Reply.class
            );
            query.setParameter("postId", postId);
            List<Reply> replies = query.getResultList();
            return replies.stream().map(ReplyDTO::new).collect(Collectors.toSet());
        }
    }
}
