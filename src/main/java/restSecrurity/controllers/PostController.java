package restSecrurity.controllers;

import io.javalin.http.Handler;
import restSecrurity.DOA.databaseDAO.PostDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.DTO.PostDTO;
import restSecrurity.DTO.ThreadDTO;
import restSecrurity.persistance.Post;
import restSecrurity.persistance.Thread;

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

    public static Handler editPostById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.queryParam("id"));
            Post toUpdateWith = ctx.bodyAsClass(Post.class);
            Post updated = postDAO.update(toUpdateWith,id);
            PostDTO updatedThreadDTO = new PostDTO(updated);
            ctx.json(updatedThreadDTO);
        };
    }

    public static Handler deleteById() {
        return ctx -> {
          int id = Integer.parseInt(ctx.queryParam("id"));
          Post deleted = postDAO.delete(id);
          PostDTO deletedPostDTO = new PostDTO(deleted);
          ctx.json(deletedPostDTO);
        };
    }
}
