package restSecrurity.controllers;

import io.javalin.http.Handler;
import restSecrurity.DOA.databaseDAO.*;
import restSecrurity.DTO.PostDTO;
import restSecrurity.DTO.ReplyDTO;
import restSecrurity.DTO.ThreadDTO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.persistance.Category;
import restSecrurity.persistance.Thread;
import restSecrurity.persistance.User;
import utills.Updater;

import java.util.List;
import java.util.Set;

public class ThreadController {

    private static ThreadDAO threadDAO;
    private static PostDAO postDAO;
    private static UserDAO userDAO;
    private static ReplyDAO replyDAO;
    private static CategoryDAO categoryDAO;
    private static ThreadController instance;

    private ThreadController() {
        // Private constructor to enforce singleton pattern
    }

    public static ThreadController getInstance(boolean isTest) {
        if (instance == null) {
            instance = new ThreadController();
            threadDAO = ThreadDAO.getInstance(isTest);
            postDAO = PostDAO.getInstance(isTest);
            userDAO = UserDAO.getInstance(isTest);
            replyDAO = ReplyDAO.getInstance(isTest);
            categoryDAO = CategoryDAO.getInstance(isTest);
        }
        return instance;
    }

    public static Handler editThreadById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ThreadDTO toUpdateWithDTO = ctx.bodyAsClass(ThreadDTO.class);
            Thread toUpdateFrom = threadDAO.getById(id);
            Thread toUpdate = Updater.updateFromDTO(toUpdateFrom, toUpdateWithDTO);
            Thread updated = threadDAO.update(toUpdate,id);
            ThreadDTO updatedThreadDTO = new ThreadDTO(updated);
            ctx.json(updatedThreadDTO);
        };
    }

    public static Handler deleteById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Thread deleted = threadDAO.delete(id);
            ThreadDTO deletedThreadDTO = new ThreadDTO(deleted);
            ctx.json(deletedThreadDTO);
        };
    }

    public static Handler createThread() {
        return ctx -> {
            try{
                ThreadDTO toCreateDTO = ctx.bodyAsClass(ThreadDTO.class);

                User threadAuthor = userDAO.getById(toCreateDTO.getUserName());
                Category category = categoryDAO.getById(Integer.parseInt(toCreateDTO.getCategory()));

                Thread toCreate = new Thread(toCreateDTO.getTitle(), toCreateDTO.getContent(), threadAuthor,category);

                Thread created = threadDAO.create(toCreate);
                ThreadDTO createdThreadDTO = new ThreadDTO(created);
                ctx.json(createdThreadDTO);
            }catch(Exception e){
                ctx.status(400);
                throw new ApiException(500,"Error while getting all threads: " + e.getMessage());
            }
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
            try {
                List<ThreadDTO> threadDTOS = threadDAO.getAll().stream().map(ThreadDTO::new).toList();
                ctx.json(threadDTOS);
            }catch (Exception e) {
                ctx.status(500);
                ctx.attribute("error",e.getMessage());
                throw new ApiException(500,"Error while getting all threads: " + e.getMessage());
            }
        };
    }

    public Handler getThreadById() {
        return ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                ThreadDTO threadDTO = new ThreadDTO(threadDAO.getById(id));
                Set<PostDTO> postDTOset = postDAO.getAllPostsByThreadId(threadDTO.getId());

                for (PostDTO postDTO : postDTOset) {
                    Set<ReplyDTO> replyDTOs = ReplyDAO.getAllRepliesByPostId(postDTO.getId());
                    postDTO.setReplies(replyDTOs);
                }
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
            try {
                String category = ctx.pathParam("category");
                List<ThreadDTO> threadDTOS = threadDAO.getByCategory(category);
                ctx.json(threadDTOS);
            }catch (Exception e) {
                ctx.status(500);
                ctx.attribute("error",e.getMessage());
                throw new ApiException(500,"Error while getting threads by Category: " + e.getMessage());
            }
        };
    }

    public Handler getThreadsByUserId(){
        return ctx -> {
            try {
                String id = ctx.pathParam("id");
                List<ThreadDTO> threadsDTOS = threadDAO.getByUserId(id);
                ctx.json(threadsDTOS);
            }catch(Exception e){
                ctx.status(500).attribute("error", e.getMessage());
                throw new ApiException(500, "Error while getting threads by userId: "+e.getMessage());
            }
        };
    }
}
