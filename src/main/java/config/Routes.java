package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;
import restSecrurity.controllers.*;
import restSecrurity.persistance.Post;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;

public class Routes {
    private static SecurityController securityController;
    private static CategoryController categoryController;
    private static PostController postController;
    private static ReplyController replyController;
    private static ThreadController threadController;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static EndpointGroup setRoutes(boolean isTest){
        return () -> {
            categoryController = CategoryController.getInstance(isTest);
            securityController = SecurityController.getInstance(isTest);
            postController = PostController.getInstance(isTest);
            replyController = ReplyController.getInstance(isTest);
            threadController = ThreadController.getInstance(isTest);

            get("/", (ctx) -> ctx.json(objectMapper.createObjectNode().put("Message", "Connected Successfully to YAPP API")), RoleType.ANYONE);
            path("/security", getSecurityRoutes());
            path("/protected",getProtectedRoutes());
            path("/public", getPublicRoutes());
        };
    }
    private static EndpointGroup getSecurityRoutes() {
        return () -> {
            path("/auth", () -> {
                post("/register", SecurityController::register, RoleType.ANYONE);
                post("/login",SecurityController::login, RoleType.ANYONE);
            });
        };
    }
    private static EndpointGroup getProtectedRoutes(){
        return () -> {
                before(SecurityController::authenticate);
                put("/editThread/{id}", ThreadController.editThreadById(),RoleType.USER,RoleType.ADMIN);
                put("/editPost/{id}", PostController.editPostById(),RoleType.USER,RoleType.ADMIN);
                put("/editReply/{id}", replyController.editReplyById(),RoleType.USER,RoleType.ADMIN);
                delete("/deleteThread/{id}", ThreadController.deleteById(),RoleType.USER,RoleType.ADMIN);
                delete("/deletePost/{id}",PostController.deleteById() ,RoleType.USER,RoleType.ADMIN);
                delete("/deleteReply/{id}",ReplyController.deleteReplyById() ,RoleType.USER,RoleType.ADMIN);
                post("/createPost",PostController.createPost(),RoleType.USER,RoleType.ADMIN);
                post("/createThread",ThreadController.createThread(),RoleType.USER,RoleType.ADMIN);
                post("/createReply",replyController.createReply(),RoleType.USER,RoleType.ADMIN);
        };
    }

    private static EndpointGroup getPublicRoutes(){
        return () -> {
                 get("/getAllThreads", threadController.getAllThreads(), RoleType.ANYONE);
                 get("/getThreadById/{id}", threadController.getThreadById(), RoleType.ANYONE);
                 get("/sortThreadByCategory/{category}", threadController.getByThreadsCategory() , RoleType.ANYONE);
                 get("/getThreads", threadController.getThreads(), RoleType.ANYONE);
                 get("/getCategories", categoryController.getAllCategories(), RoleType.ANYONE);
                 get("/getUserById/{id}", securityController.getUserById(), RoleType.USER,RoleType.ADMIN);
                 get("/getThreadsByUserId/{id}", threadController.getThreadsByUserId(), RoleType.ANYONE);
                 get("/getPostsByUserId/{id}", postController.getPostsByUserId(),RoleType.ANYONE);
        };
    }

    public enum RoleType implements RouteRole {
        USER,
        ADMIN,
        ANYONE
    }
}
