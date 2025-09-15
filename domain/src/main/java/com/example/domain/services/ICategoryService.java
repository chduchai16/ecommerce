package com.example.domain.services;

import com.example.domain.models.entities.Category;
import java.util.List;

public interface ICategoryService {
    Category getCategoryById(int categoryId) throws Exception;
    List<Category> getAllCategories() ;
    Category createCategory(Category category) throws Exception;
    Category updateCategory(Category category) throws Exception;
    void deleteCategory(int categoryId) throws Exception;
}
