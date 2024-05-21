package restSecrurity.DOA.databaseDAO;

import restSecrurity.persistance.Reply;

public class ReplyDAO extends DAO<Reply,Integer> {

    private static ReplyDAO instance;

    public ReplyDAO(boolean isTest) {
        super(Reply.class,false);
    }
    public static ReplyDAO getInstance(boolean isTest){
        if(instance == null){
            instance = new ReplyDAO(isTest);
        }
        return instance;
    }
}
