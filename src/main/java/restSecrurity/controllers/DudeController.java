package restSecrurity.controllers;

import io.javalin.http.Handler;
import restSecrurity.DOA.databaseDAO.DudeDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.DOA.memoryDAO.DudeMemoryDAO;
import restSecrurity.model.Dude;

import java.util.List;

public class DudeController {
    private static iDAO<Dude, Integer> dudeDAO;
    private static DudeController instance;

    private DudeController() {
        // Private constructor to enforce singleton pattern
    }
    public static DudeController getInstance(boolean memory,boolean isTest) {
        if (instance == null) {
            instance = new DudeController();
            if(memory) {
                dudeDAO = DudeMemoryDAO.getInstance();
            }else {
                dudeDAO = DudeDAO.getInstance(isTest);
            }
        }
        return instance;
    }

    public Handler getAll() {
        return ctx -> {
            List<Dude> dudeList = null;
            dudeList = dudeDAO.getAll();
            ctx.json(dudeList);
        };
    }

    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Dude dude = dudeDAO.getById(id);
            ctx.json(dude);
        };
    }
}
