package restSecrurity.controllers;

import io.javalin.http.Handler;
import restSecrurity.DOA.databaseDAO.ReplyDAO;
import restSecrurity.DOA.databaseDAO.ThreadDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.persistance.Reply;
import restSecrurity.persistance.Thread;

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
            
        };
    }
}
