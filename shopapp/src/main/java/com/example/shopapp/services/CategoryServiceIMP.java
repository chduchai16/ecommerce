package com.example.shopapp.services;

import com.example.shopapp.models.entities.Category;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.specifications.CategorySpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        return this.categoryRepository.findAll() ;
    }

    @Override
    public Page<Category> filterCategories(
            String name ,
            Pageable pageable
    ) {
        Specification<Category> spec = Specification.where(
                CategorySpecification.hasName(name)
        ) ;
        return categoryRepository.findAll(spec , pageable);
    }

    @Override
    public Category createCategory(Category category) throws Exception {

        Specification<Category> spec = Specification.where(
                CategorySpecification.hasExactName(category.getName())
        ) ;

        boolean exists = categoryRepository.findOne(spec).isPresent() ;

        if(exists) {
            throw new DataIntegrityViolationException("Tên loại hàng không được trùng lặp") ;
        }
        return categoryRepository.save(category) ;
    }

    @Override
    public Category updateCategory(Category category) throws Exception {

        if(category.getId() == 0) {
            throw new IllegalArgumentException("ID loại hàng không được để trống khi cập nhật") ;
        }

        Specification<Category> spec = Specification.where(
                CategorySpecification.hasExactName(category.getName())
        ) ;

        Optional<Category> existingCategory = categoryRepository.findOne(spec) ;

        if(existingCategory.isEmpty()) {
            throw new EntityNotFoundException("Loại hàng không tồn tại") ;
        }

        if(!Objects.equals(existingCategory.get().getId(), category.getId())) {
            throw new DataIntegrityViolationException("Tên loại hàng không được trùng lặp") ;
        }
        return categoryRepository.save(category) ;
    }

    @Override
    public void deleteCategory(int categoryId) throws Exception {
        categoryRepository.findById(categoryId).orElseThrow(()->new EntityNotFoundException("Loại hàng này không tồn tại")) ;
        categoryRepository.deleteById(categoryId);
    }
}
