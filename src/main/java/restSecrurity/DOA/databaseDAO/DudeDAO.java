package restSecrurity.DOA.databaseDAO;

import restSecrurity.DOA.memoryDAO.DudeMemoryDAO;
import restSecrurity.model.Dude;

public class DudeDAO extends DAO<Dude,Integer> {

    private static DudeDAO instance;

    public DudeDAO(boolean isTest) {
        super(Dude.class,false);
    }
    public static DudeDAO getInstance(boolean isTest){
        if(instance == null){
            instance = new DudeDAO(isTest);
        }
        return instance;
    }
}
