package com.example.exona_tech.imp_services;

import com.example.domain.dtos.requests.CategoryDTO;
import com.example.domain.entities.Category;
import com.example.domain.repositories.CategoryRepository;
import com.example.domain.services.ICategoryService;
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
    public Category createCategory(CategoryDTO categoryDTO) throws Exception {
        if(categoryRepository.findByName(categoryDTO.getName()).isEmpty()){
            Category category = new Category();
            category.setName(categoryDTO.getName());
            return categoryRepository.save(category) ;
        }else {
            throw new Exception("Category's name cannot duplicate") ;
        }
    }

    @Override
    public Category updateCategory(int categoryId, CategoryDTO categoryDTO) throws Exception {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new Exception("Category does not exist"));
        if(categoryRepository.findByName(categoryDTO.getName()).isEmpty()) {
            existingCategory.setName(categoryDTO.getName());
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
