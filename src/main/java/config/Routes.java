package config;

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


    public static EndpointGroup setRoutes(boolean isTest){
        return () -> {
            categoryController = CategoryController.getInstance(isTest);
            securityController = SecurityController.getInstance(isTest);
            postController = PostController.getInstance(isTest);
            replyController = ReplyController.getInstance(isTest);
            threadController = ThreadController.getInstance(isTest);

            get("/", (ctx) -> ctx.json("Successfully Connected to Yapp"), RoleType.ANYONE);
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
                post("/editThread/{id}", ThreadController.editThreadById(),RoleType.USER);
                post("/editPost/{id}", PostController.editPostById(),RoleType.USER);
                post("/deleteThread/{id}", ThreadController.deleteById(),RoleType.USER,RoleType.ADMIN);
                post("/deletePost/{id}",PostController.deleteById() ,RoleType.USER,RoleType.ADMIN);
                post("/createPost",PostController.createPost(),RoleType.USER);
                post("/createThread",ThreadController.createThread(),RoleType.USER);
        };
    }

    private static EndpointGroup getPublicRoutes(){
        return () -> {
                 get("/getAllThreads", threadController.getAllThreads(), RoleType.ANYONE);
                 get("/getThreadById/{id}", threadController.getThreadById(), RoleType.ANYONE);
                 get("/sortThreadByCategory/{category}", threadController.getByThreadsCategory() , RoleType.ANYONE);
                 get("/getThreads", threadController.getThreads(), RoleType.ANYONE);
                 get("/getCategories", categoryController.getAllCategories(), RoleType.ANYONE);
                 get("/getUserById/{id}", securityController.getUserById(), RoleType.ANYONE);
                 get("/getThreadsByUserId/{id}", threadController.getThreadsByUserId(), RoleType.ANYONE);
        };
    }

    public enum RoleType implements RouteRole {
        USER,
        ADMIN,
        ANYONE
    }
}
