package com.example.exona_tech.imp_services;

import com.example.exona_tech.dtos.requests.CategoryDTO;
import com.example.domain.entities.Category;
import com.example.domain.repositories.CategoryRepository;
import com.example.domain.services.ICategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceIMP implements ICategoryService {

    private final CategoryRepository categoryRepository ;

    @Override
    public Category getCategoryById(int categoryId) throws Exception {
        return categoryRepository.findById(categoryId).orElseThrow(() ->new Exception("Cannot find this category"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) throws Exception {
        if(categoryRepository.findByName(category.getName()).isEmpty()){
            return categoryRepository.save(category) ;
        }else {
            throw new Exception("Category's name cannot duplicate") ;
        }
    }

    @Override
    public Category updateCategory(Category category) throws Exception {
        Category existingCategory = categoryRepository.findById(category.getId()).orElseThrow(() -> new Exception("Category does not exist"));
        if(categoryRepository.findByName(category.getName()).isEmpty()) {
            existingCategory.setName(category.getName());
            return categoryRepository.save(existingCategory) ;
        }
        else {
            throw new Exception("Category's name cannot duplicate") ;
        }
    }

    @Override
    public void deleteCategory(int categoryId) throws Exception {
        categoryRepository.findById(categoryId).orElseThrow(()->new Exception("This Category does not exist")) ;
        categoryRepository.deleteById(categoryId);
    }
}
