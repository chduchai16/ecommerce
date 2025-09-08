package com.example.exona_tech.controllers;

import com.example.exona_tech.dtos.requests.CategoryDTO;
import com.example.exona_tech.dtos.resposnes.BaseResponse;
import com.example.exona_tech.dtos.resposnes.CategoryResponse;
import com.example.domain.entities.Category;
import com.example.domain.services.ICategoryService;
import com.example.exona_tech.mappers.CategoryMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.api-prefix}/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "Quản lý loại hàng hóa trong hệ thống")

public class CategoryController {
    private final ICategoryService categoryService ;
    private final CategoryMapper categoryMapper ;

    @GetMapping()
    public ResponseEntity<?> getAllCategories(){
        try{
            List<Category> categories = categoryService.getAllCategories();
            List<CategoryResponse> categoryResponses = categories
                    .stream()
                    .map(categoryMapper :: fromEntityToResponse)
                    .toList();
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Create category successfully." ,categoryResponses);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e){
            System.out.println("Error get all category: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Get category failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
    // lấy thông tin chi tiết category theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") int categoryId){
        try{
            Category category = categoryService.getCategoryById(categoryId);
            CategoryResponse categoryResponse = categoryMapper.fromEntityToResponse(category) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Get category successfully.",categoryResponse);
            return ResponseEntity.ok(baseResponse);
        }
        catch (Exception e){
            System.out.println("Error get category: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Get category failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // tạo category
    @PostMapping()
    public ResponseEntity<?> createCategory(
            @RequestBody @Valid CategoryDTO categoryDTO,
            BindingResult result
    ){
        try{
            if (result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder() ;
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println("Create : invalid data " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Category category = categoryService.createCategory(categoryMapper.fromRequestToEntity(categoryDTO));
            CategoryResponse categoryResponse = categoryMapper.fromEntityToResponse(category) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Create category successfully.",categoryResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(Exception e) {
            System.err.println("Error creating category: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Create category failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // cập nật category
    @PutMapping()
    public ResponseEntity<?> updateCategory (
            @RequestBody @Valid CategoryDTO categoryDTO,
            BindingResult result
    ){
        try{
            if (result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder() ;
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println("Updating : invalid data " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Category category = categoryService.updateCategory(categoryMapper.fromRequestToEntity(categoryDTO)) ;
            CategoryResponse categoryResponse = categoryMapper.fromEntityToResponse(category) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Update category successfully." ,categoryResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(Exception e) {
            System.err.println("Error updating category: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Create category failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // xoá category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") int categoryId){
        try{
            categoryService.deleteCategory(categoryId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Delete category successfully.");
            return ResponseEntity.ok().body(baseResponse) ;
        }
        catch(Exception e){
            System.out.println("Error deleting category: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Delete category failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }


}
