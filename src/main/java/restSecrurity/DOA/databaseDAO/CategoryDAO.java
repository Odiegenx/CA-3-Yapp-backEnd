package restSecrurity.DOA.databaseDAO;

import restSecrurity.persistance.Category;

public class CategoryDAO extends DAO<Category, Integer> {

    private static CategoryDAO instance;

    public CategoryDAO(boolean isTest) {
        super(Category.class,false);
    }
    public static CategoryDAO getInstance(boolean isTest){
        if(instance == null){
            instance = new CategoryDAO(isTest);
        }
        return instance;
    }

}
