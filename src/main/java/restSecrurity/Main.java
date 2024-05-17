package restSecrurity;

import config.ApplicationConfig;
import config.Routes;
import restSecrurity.DOA.memoryDAO.DudeMemoryDAO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.model.Dude;

public class Main {
    public static void main(String[] args) {
        DudeMemoryDAO memoryDAO = DudeMemoryDAO.getInstance();
        Dude d1 = new Dude("Hans");
        Dude d2 = new Dude("Michael");
        Dude d3 = new Dude("John");
        try {
            memoryDAO.create(d1);
            memoryDAO.create(d2);
            memoryDAO.create(d3);
        }catch (ApiException e){
            System.out.println(e.getMessage());
        }

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
