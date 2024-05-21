package restSecrurity;

import config.ApplicationConfig;
import config.HibernateConfig;
import config.Routes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import restSecrurity.DOA.memoryDAO.DudeMemoryDAO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.model.Dude;
import utills.Populator;

public class Main {
    public static void main(String[] args) {
        //Populator.populateDatabase(false);
        // hey
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
