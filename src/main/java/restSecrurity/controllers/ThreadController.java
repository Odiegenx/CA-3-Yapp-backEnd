package restSecrurity.controllers;

import restSecrurity.DOA.databaseDAO.ThreadDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.persistance.Thread;

public class ThreadController {

    private static iDAO<Thread, Integer> threadDAO;
    private static ThreadController instance;

    private ThreadController() {
        // Private constructor to enforce singleton pattern
    }

    public static ThreadController getInstance(boolean isTest) {
        if (instance == null) {
            instance = new ThreadController();
            threadDAO = ThreadDAO.getInstance(isTest);
        }
        return instance;
    }

    }
