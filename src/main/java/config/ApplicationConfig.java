package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import restSecrurity.exceptions.ApiException;
import restSecrurity.DTO.UserDTO;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import restSecrurity.exceptions.logger.Logger;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ApplicationConfig {
    ObjectMapper om = new ObjectMapper();
    private Javalin app;
    private static ApplicationConfig instance;
    private ApplicationConfig(){
    }
    // now it's a singleton
    public static ApplicationConfig getInstance(){
        if(instance == null){
            instance = new ApplicationConfig();
        }
        return instance;
    }

    public ApplicationConfig initiateServer(){
        app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.routing.contextPath = "/api"; // what ever you want your urls starts with
        });
        app.attribute("bye","Goodbye");

        /*
        Middleware (HTTP Filters):

        Middleware in Javalin allows us to add common functionalities like logging,
        authentication, etc., to our web application.

        */
        app.before( ctx -> {
            HttpServletRequest request = ctx.req();
            System.out.println(request);
        });
        /*
        Here we print out the request before its processed by the route handler.
         */
        app.after(ctx -> {
            HttpServletResponse response = ctx.res();
            System.out.println("----");
            String goodbye = app.attribute("bye");
            System.out.println(goodbye);
            System.out.println(response);
        });
        /*
        similarly to app.before, after logs the response to the console
        after the request has been handled.
         */
        return instance;
    }
    public ApplicationConfig setRoute(EndpointGroup route){
        app.routes(route);
        return instance;
    }

    /*
    Exception Handling:
     */
    public ApplicationConfig setExceptionOverallHandling(){
        app.exception(ApiException.class,(e,ctx) ->{
            ObjectNode error = Logger.exceptionLog(e.getStatusCode(),e.getMessage());
            ctx.status(e.getStatusCode()).json(error);
        });
        app.exception(NumberFormatException.class,(e,ctx) ->{
            ObjectNode node = om.createObjectNode().put("Bad request: Not a number!",e.getMessage());
            ctx.status(400).json(node);
        });
        app.exception(NullPointerException.class,(e,ctx) -> {
            ObjectNode node = om.createObjectNode().put("Bad request: Not found!",e.getMessage());
            ctx.status(404).json(node);
        });
        app.exception(Exception.class, (e,ctx) ->{
            ObjectNode node = om.createObjectNode().put("errorMessage",e.getMessage());
            ctx.status(500).json(node);
        });
        return instance;
    }

    // public ApplicationConfig setException
    public ApplicationConfig startServer(int port){
        app.start(port);
        return instance;
    }
    public ApplicationConfig configureCors() {
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        app.options("/*", ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });
        return instance;
    }

    public ApplicationConfig checkSecurityRoles() {
        ObjectMapper jsonMapper = new ObjectMapper();
        // Check roles on the user (ctx.attribute("username") and compare with permittedRoles using securityController.authorize()
        app.updateConfig(config -> {
            config.accessManager((handler, ctx, permittedRoles) -> {
                // permitted roles are defined in the last arg to routes: get("/", ctx -> ctx.result("Hello World"), Role.ANYONE);

                Set<String> allowedRoles = permittedRoles.stream().map(role -> role.toString().toUpperCase()).collect(Collectors.toSet());
                if(allowedRoles.contains("ANYONE") || ctx.method().toString().equals("OPTIONS")) {
                    // Allow requests from anyone and OPTIONS requests (preflight in CORS)
                    handler.handle(ctx);
                    return;
                }
                UserDTO user = ctx.attribute("user");
                System.out.println("USER IN CHECK_SEC_ROLES: "+user);
                if(user == null) {
                    ctx.status(HttpStatus.FORBIDDEN)
                            .json(jsonMapper.createObjectNode()
                                    .put("msg", "Not authorized. No username were added from the token"));
                }
                if (authorize(user, allowedRoles)) {
                    handler.handle(ctx);
                }else {
                    try {
                        throw new ApiException(HttpStatus.FORBIDDEN.getCode(), "Only authorized for roles: " + allowedRoles);
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        return instance;
    }
    private static boolean authorize(UserDTO user, Set<String> allowedRoles) {
        AtomicBoolean hasAcces = new AtomicBoolean(false);
        if(user != null){
            user.getRoles().forEach(role ->{
                if(allowedRoles.contains(role/*.getName()*/.toUpperCase())){
                    hasAcces.set(true);
                }
            });
        }
        return hasAcces.get();
    }

}
