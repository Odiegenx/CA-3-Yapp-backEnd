package restSecrurity.controllers;

import io.javalin.http.Handler;
import restSecrurity.DOA.databaseDAO.ReplyDAO;
import restSecrurity.DOA.databaseDAO.ThreadDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.DTO.PostDTO;
import restSecrurity.DTO.ReplyDTO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.persistance.Post;
import restSecrurity.persistance.Reply;
import restSecrurity.persistance.Thread;
import utills.Updater;

public class ReplyController {
    private static iDAO<Reply, Integer> replyDAO;
    private static ReplyController instance;

    private ReplyController() {
        // Private constructor to enforce singleton pattern
    }

    public static ReplyController getInstance(boolean isTest) {
        if (instance == null) {
            instance = new ReplyController();
            replyDAO = ReplyDAO.getInstance(isTest);
        }
        return instance;
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
}
