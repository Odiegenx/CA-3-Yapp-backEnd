package restSecrurity.controllers;

import restSecrurity.DOA.databaseDAO.PostDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.persistance.Post;

public class PostController {
    private static iDAO<Post, Integer> postDAO;
    private static PostController instance;

    private PostController() {
        // Private constructor to enforce singleton pattern
    }

    public static PostController getInstance(boolean isTest) {
        if (instance == null) {
            instance = new PostController();
            postDAO = PostDAO.getInstance(isTest);
        }
        return instance;
    }
}
