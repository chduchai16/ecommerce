package com.example.shopapp.imp_services;

import com.example.domain.models.entities.Category;
import com.example.domain.persistence.repositories.CategoryRepository;
import com.example.domain.services.ICategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceIMP implements ICategoryService {

    private final CategoryRepository categoryRepository ;

    @Override
    public Category getCategoryById(int categoryId) throws Exception {
        return categoryRepository.findById(categoryId).orElseThrow(() ->new EntityNotFoundException("Không tìm thấy loại hàng này"));
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
            throw new DataIntegrityViolationException("Tên loại hàng không được trùng lặp") ;
        }
    }

    @Override
    public Category updateCategory(Category category) throws Exception {
        Category existingCategory = categoryRepository.findById(category.getId()).orElseThrow(() -> new EntityNotFoundException("Loại hàng không tồn tại"));
        if(categoryRepository.findByName(category.getName()).isPresent() && !existingCategory.getName().equals(category.getName())) {
            throw new DataIntegrityViolationException("Tên loại hàng không được trùng lặp") ;
        }
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory) ;
    }

    @Override
    public void deleteCategory(int categoryId) throws Exception {
        categoryRepository.findById(categoryId).orElseThrow(()->new EntityNotFoundException("Loại hàng này không tồn tại")) ;
        categoryRepository.deleteById(categoryId);
    }
}
