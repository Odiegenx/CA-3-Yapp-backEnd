package restSecrurity.controllers;

import restSecrurity.DOA.databaseDAO.CategoryDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.persistance.Category;

public class CategoryController {
    private static iDAO<Category, Integer> categoryDAO;
    private static CategoryController instance;

    private CategoryController() {
        // Private constructor to enforce singleton pattern
    }

    public static CategoryController getInstance(boolean isTest) {
        if (instance == null) {
            instance = new CategoryController();
            categoryDAO = CategoryDAO.getInstance(isTest);
        }
        return instance;
    }
}
