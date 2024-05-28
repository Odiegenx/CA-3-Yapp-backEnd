package restSecrurity.controllers;

import io.javalin.http.Handler;
import restSecrurity.DOA.databaseDAO.PostDAO;
import restSecrurity.DOA.databaseDAO.ReplyDAO;
import restSecrurity.DOA.databaseDAO.ThreadDAO;
import restSecrurity.DOA.databaseDAO.UserDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.DTO.PostDTO;
import restSecrurity.DTO.ThreadDTO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.persistance.Post;
import restSecrurity.persistance.Reply;
import restSecrurity.persistance.Thread;
import restSecrurity.persistance.User;
import utills.Updater;

import java.util.List;

public class PostController {
    private static PostDAO postDAO;
    private static iDAO<Thread, Integer> threadDAO;
    private static iDAO<User, String> userDAO;
    private static iDAO<Reply, Integer> replyDAO;
    private static PostController instance;

    private PostController() {
        // Private constructor to enforce singleton pattern
    }

    public static PostController getInstance(boolean isTest) {
        if (instance == null) {
            instance = new PostController();
            postDAO = PostDAO.getInstance(isTest);
            threadDAO = ThreadDAO.getInstance(isTest);
            userDAO = UserDAO.getInstance(isTest);
            replyDAO = ReplyDAO.getInstance(isTest);
        }
        return instance;
    }

    public static Handler editPostById() {
        return ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                PostDTO toUpdateWithDTO = ctx.bodyAsClass(PostDTO.class);
                Post toUpdateFrom = postDAO.getById(id);
                Post toUpdate = Updater.updateFromDTO(toUpdateFrom, toUpdateWithDTO);
                Post updated = postDAO.update(toUpdate, id);
                PostDTO updatedThreadDTO = new PostDTO(updated);
                ctx.json(updatedThreadDTO);
            } catch (Exception e) {
                ctx.status(500);
                ctx.json(new ApiException(500, "Error while editing post: " + e.getMessage()));
            }
        };
    }

    public static Handler deleteById() {
        return ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                Post deleted = postDAO.delete(id);
                PostDTO deletedPostDTO = new PostDTO(deleted);
                ctx.json(deletedPostDTO);
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500, "Error while deleting post: " + e.getMessage());
            }
        };
    }

    public static Handler createPost() {
        return ctx -> {
            try {
                PostDTO toCreateDTO = ctx.bodyAsClass(PostDTO.class);
                User postAuthor = userDAO.getById(toCreateDTO.getUserName());
                Thread postThread = threadDAO.getById(toCreateDTO.getThreadId());

                if (toCreateDTO.getParentReplyId() == null) {
                    Post newPost = new Post(toCreateDTO.getContent(), postAuthor, postThread);
                    Post createdPost = postDAO.create(newPost);
                    PostDTO postDTO = new PostDTO(createdPost);
                    ctx.json(postDTO);
                } else {
                    Post parentPost = postDAO.getById(toCreateDTO.getParentReplyId());
                    Reply newReply = new Reply(toCreateDTO.getContent(), parentPost, postAuthor);
                    replyDAO.create(newReply);
                    parentPost.addReply(newReply);
                    postDAO.update(parentPost, parentPost.getId());

                    Post newPost = new Post(toCreateDTO.getContent(), postAuthor, postThread, parentPost);
                    Post createdPost = postDAO.create(newPost);
                    PostDTO postDTO = new PostDTO(createdPost);
                    ctx.json(postDTO);
                }
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500, "Unable to create new post: " + e.getMessage());
            }
        };
    }
}

