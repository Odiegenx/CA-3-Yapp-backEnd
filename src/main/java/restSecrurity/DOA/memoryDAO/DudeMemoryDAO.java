package restSecrurity.DOA.memoryDAO;

import restSecrurity.model.Dude;

public class DudeMemoryDAO extends MemoryDAO<Dude,Integer> {

    private static DudeMemoryDAO instance;

    public DudeMemoryDAO() {
        super(Dude.class);
    }
    public static DudeMemoryDAO getInstance(){
        if(instance == null){
            instance = new DudeMemoryDAO();
        }
        return instance;
    }
}
