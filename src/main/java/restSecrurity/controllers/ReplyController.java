package restSecrurity.controllers;

import io.javalin.http.Handler;
import org.hibernate.Hibernate;
import restSecrurity.DOA.databaseDAO.PostDAO;
import restSecrurity.DOA.databaseDAO.ReplyDAO;
import restSecrurity.DOA.databaseDAO.ThreadDAO;
import restSecrurity.DOA.databaseDAO.UserDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.DTO.PostDTO;
import restSecrurity.DTO.ReplyDTO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.persistance.Post;
import restSecrurity.persistance.Reply;
import restSecrurity.persistance.Thread;
import restSecrurity.persistance.User;
import utills.Updater;


public class ReplyController {
    private static iDAO<Reply, Integer> replyDAO;
    private static ReplyController instance;
    private static UserDAO userDAO;
    private static ThreadDAO threadDAO;
    private static PostDAO postDAO;

    private ReplyController() {
        // Private constructor to enforce singleton pattern
    }

    public static ReplyController getInstance(boolean isTest) {
        if (instance == null) {
            instance = new ReplyController();
            replyDAO = ReplyDAO.getInstance(isTest);
            userDAO = UserDAO.getInstance(isTest);
            threadDAO = ThreadDAO.getInstance(isTest);
            postDAO = PostDAO.getInstance(isTest);
        }
        return instance;
    }

    public static Handler deleteReplyById() {
        return ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                Reply toBeDeleted = replyDAO.getById(id);
                Post post = postDAO.getById(toBeDeleted.getParentPost().getId());
                post.getReplies().removeIf(r -> r.getId() == id);
                Post updatedPost = postDAO.update(post, post.getId());
                ReplyDTO deletedReplyDTO = new ReplyDTO(toBeDeleted);
                ctx.json(deletedReplyDTO);
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500, "Error while deleting reply: " + e.getMessage());
            }
        };
    }

    public Handler editReplyById() {
        return ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                ReplyDTO toUpdateWithDTO = ctx.bodyAsClass(ReplyDTO.class);
                Reply toUpdateFrom = replyDAO.getById(id);
                Reply toUpdate = Updater.updateFromDTO(toUpdateFrom, toUpdateWithDTO);
                Reply updated = replyDAO.update(toUpdate, id);
                ReplyDTO updatedThreadDTO = new ReplyDTO(updated);
                ctx.json(updatedThreadDTO);
            } catch (Exception e) {
                ctx.status(500);
                ctx.json(new ApiException(500, "Error while editing reply: " + e.getMessage()));
            }
        };
    }

    public Handler createReply() {
        return ctx -> {
            try {
                ReplyDTO toCreateDTO = ctx.bodyAsClass(ReplyDTO.class);
                User postAuthor = userDAO.getById(toCreateDTO.getUserName());
                Post parentPost = postDAO.getById(toCreateDTO.getParentPostId());
                Reply newReply = new Reply(toCreateDTO.getContent(), parentPost, postAuthor);
                Reply created = replyDAO.create(newReply);
                Hibernate.initialize(parentPost.getReplies());
                parentPost.addReply(newReply);
                postDAO.update(parentPost, parentPost.getId());

                ReplyDTO createdDTO = new ReplyDTO(created);
                ctx.json(createdDTO);
            } catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500, "Unable to create new post: " + e.getMessage());
            }
        };
    }
}
