package restSecrurity.DOA.databaseDAO;

import restSecrurity.persistance.Thread;

public class ThreadDAO extends DAO<Thread,Integer> {
    private static ThreadDAO instance;

    public ThreadDAO(boolean isTest) {
        super(Thread.class,false);
    }
    public static ThreadDAO getInstance(boolean isTest){
        if(instance == null){
            instance = new ThreadDAO(isTest);
        }
        return instance;
    }


}
