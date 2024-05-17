package utills;

import config.HibernateConfig;
import jakarta.persistence.EntityManager;
import restSecrurity.DOA.memoryDAO.DudeMemoryDAO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.model.Dude;

import java.util.ArrayList;
import java.util.List;

public class Populator {

    private static EntityManager entityManager;
    private static DudeMemoryDAO dudeMemoryDAO;

    public static void populateDatabase(boolean isMemory,boolean isTest)  {
        List<Dude> dudes = createDudes();
        if(isMemory){
            dudeMemoryDAO = DudeMemoryDAO.getInstance();

            for (Dude dude : dudes) {
                try {
                    dudeMemoryDAO.create(dude);
                }catch (ApiException e){
                    e.printStackTrace();
                }
            }
        }else {
            entityManager = HibernateConfig.getEntityManagerFactoryConfig(isTest).createEntityManager();

            for (Dude dude : dudes) {
                entityManager.persist(dude);
            }
        }
    }

    private static List<Dude> createDudes() {
        List<Dude> dudes = new ArrayList<>();
        dudes.add(new Dude("John"));
        dudes.add(new Dude("Mike"));
        dudes.add(new Dude("Sarah"));
        dudes.add(new Dude("Emily"));
        dudes.add(new Dude("Alex"));
        return dudes;
    }
}
