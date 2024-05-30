package restSecrurity;

import config.ApplicationConfig;
import config.Routes;

public class Main {
    public static void main(String[] args) {
        //Populator.populateDatabase(false);
        startServer(7070);
    }
    private static void startServer(int port) {
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig.initiateServer()
                .startServer(port)
                .setExceptionOverallHandling()
                .checkSecurityRoles()
                .setRoute(Routes.setRoutes(false))
                .configureCors();
    }
}
