package config;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;
import restSecrurity.controllers.DudeController;
import restSecrurity.controllers.SecurityController;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;

public class Routes {
    private static SecurityController securityController;
    private static DudeController dudeController;

    public static EndpointGroup setRoutes(boolean isTest){
        return () -> {
            securityController = SecurityController.getInstance(isTest);
            dudeController = DudeController.getInstance(true,false);
            before(SecurityController::authenticate);
            path("/security", getSecurityRoutes());
            path("/protected",getProtectedRoutes());
            path("/memoryStorage",getMemoryStorageTestRoutes());
        };
    }
    private static EndpointGroup getSecurityRoutes() {
        return () -> {
            path("/auth", () -> {
                // before(SecurityController::authenticate);
                post("/register", SecurityController::register, RoleType.ANYONE);
                post("/login",SecurityController::login, RoleType.ANYONE);
            });
        };
    }
    private static EndpointGroup getProtectedRoutes(){
        return () -> {
            path("/protected",() ->{
                // before(SecurityController::authenticate);
                get("/user", ctx -> ctx.result("great success from User Route"),RoleType.USER);
                get("/admin", ctx -> ctx.result("great success from Admin Route"),RoleType.ADMIN);
            });
        };
    }

    private static EndpointGroup getMemoryStorageTestRoutes(){
        return () -> {
            get("/", ctx -> ctx.json("Connection success"), RoleType.ANYONE);
            get("/getbyid/{id}", dudeController.getById(), RoleType.ANYONE);
        };
    }

    public enum RoleType implements RouteRole {
        USER,
        ADMIN,
        ANYONE
    }
}
