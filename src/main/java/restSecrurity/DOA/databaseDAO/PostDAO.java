package restSecrurity.DOA.databaseDAO;

import restSecrurity.persistance.Category;
import restSecrurity.persistance.Post;

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
}
