package restSecrurity.controllers;

import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;
import restSecrurity.DOA.databaseDAO.PostDAO;
import restSecrurity.DOA.databaseDAO.ThreadDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.DTO.PostDTO;
import restSecrurity.DTO.ThreadDTO;
import restSecrurity.persistance.Post;
import restSecrurity.persistance.Thread;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ThreadController {

    private static ThreadDAO threadDAO;
    private static PostDAO postDAO;
    private static ThreadController instance;

    private ThreadController() {
        // Private constructor to enforce singleton pattern
    }

    public static ThreadController getInstance(boolean isTest) {
        if (instance == null) {
            instance = new ThreadController();
            threadDAO = ThreadDAO.getInstance(isTest);
            postDAO = PostDAO.getInstance(isTest);
        }
        return instance;
    }

    public static Handler editThreadById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.queryParam("id"));
            Thread toUpdateWith = ctx.bodyAsClass(Thread.class);
            Thread updated = threadDAO.update(toUpdateWith,id);
            ThreadDTO updatedThreadDTO = new ThreadDTO(updated);
            ctx.json(updatedThreadDTO);
        };
    }

    public static Handler deleteById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.queryParam("id"));
            Thread deleted = threadDAO.delete(id);
            ThreadDTO deletedThreadDTO = new ThreadDTO(deleted);
            ctx.json(deletedThreadDTO);
        };
    }

    public Handler getThreads() {
        return ctx -> {
            try {
                int count = Integer.parseInt(ctx.queryParam("count"));
                int size = Integer.parseInt(ctx.queryParam("size"));
                List<ThreadDTO> threads = threadDAO.getThreads(count, size);
                ctx.json(threads);
            }catch (NumberFormatException e) {
                ctx.attribute("error",e.getMessage());
                ctx.status(400);
            }

        };
    }

    public Handler getAllThreads() {
        return ctx -> {
            List<ThreadDTO> threadDTOS = threadDAO.getAll().stream().map(ThreadDTO::new).toList();
            ctx.json(threadDTOS);
        };
    }

    public Handler getThreadById() {
        return ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                ThreadDTO threadDTO = new ThreadDTO(threadDAO.getById(id));
                Set<PostDTO> postDTOset = postDAO.getAllPostsByThreadId(threadDTO.getId());
                threadDTO.setPosts(postDTOset);
                ctx.json(threadDTO);
            }catch (NumberFormatException e) {
                ctx.attribute("error",e.getMessage());
                ctx.status(400);
            }
        };
    }

    public Handler getByThreadsCategory() {
        return ctx -> {
            String category = ctx.pathParam("category");
            List<ThreadDTO> threadDTOS = threadDAO.getByCategory(category);
            ctx.json(threadDTOS);
        };
    }
}
