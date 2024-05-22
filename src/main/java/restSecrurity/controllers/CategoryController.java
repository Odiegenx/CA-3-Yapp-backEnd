package restSecrurity.controllers;

import io.javalin.http.Handler;
import restSecrurity.DOA.databaseDAO.CategoryDAO;
import restSecrurity.DOA.iDAO;
import restSecrurity.DTO.CategoryDTO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.persistance.Category;

import java.util.ArrayList;
import java.util.List;

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

    public Handler getAllCategories() {
        return ctx -> {
            try{
                List<Category> categories = categoryDAO.getAll();
                List<CategoryDTO> categoriesDTO = categories.stream().map(CategoryDTO::new).toList();
                ctx.json(categoriesDTO);
            }catch (Exception e) {
                ctx.status(500);
                throw new ApiException(500, e.getMessage());
            }
        };
    }
}
