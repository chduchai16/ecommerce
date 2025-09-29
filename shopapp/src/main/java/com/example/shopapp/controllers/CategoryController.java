package com.example.shopapp.controllers;

import com.example.domain.models.entities.Category;
import com.example.domain.services.ICategoryService;
import com.example.shopapp.transfer.dtos.requests.CategoryDTO;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import com.example.shopapp.transfer.dtos.responses.CategoryResponse;
import com.example.shopapp.transfer.mappers.CategoryMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.api-prefix}/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "Quản lý danh mục hàng hóa trong hệ thống")

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
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Lấy danh sách danh mục thành công." ,categoryResponses);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e){
            System.out.println("Lỗi lấy danh sách danh mục: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
    // lấy thông tin chi tiết category theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") int categoryId){
        try{
            Category category = categoryService.getCategoryById(categoryId);
            CategoryResponse categoryResponse = categoryMapper.fromEntityToResponse(category) ;
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Lấy thông tin danh mục thành công.",categoryResponse);
            return ResponseEntity.ok(baseResponse);
        }
        catch (EntityNotFoundException e){
            System.out.println("Lỗi lấy thông tin danh mục: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(404 , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (Exception e){
            System.out.println("Lỗi lấy thông tin danh mục: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " + e.getMessage());
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
                System.out.println("Tạo : dữ liệu không hợp lệ " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse(400 , "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Category category = categoryService.createCategory(categoryMapper.fromRequestToEntity(categoryDTO));
            CategoryResponse categoryResponse = categoryMapper.fromEntityToResponse(category) ;
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Tạo danh mục thành công.",categoryResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(DataIntegrityViolationException e) {
            System.err.println("Lỗi tạo danh mục: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(409 , e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        }
        catch(Exception e) {
            System.err.println("Lỗi tạo danh mục: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " + e.getMessage());
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
                System.out.println("Cập nhật : dữ liệu không hợp lệ " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse(400 , "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Category category = categoryService.updateCategory(categoryMapper.fromRequestToEntity(categoryDTO)) ;
            CategoryResponse categoryResponse = categoryMapper.fromEntityToResponse(category) ;
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Cập nhật danh mục thành công." ,categoryResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(EntityNotFoundException e) {
            System.err.println("Lỗi cập nhật danh mục: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(404 , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch(DataIntegrityViolationException e) {
            System.err.println("Lỗi cập nhật danh mục: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(409 , e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        }
        catch(Exception e) {
            System.err.println("Lỗi cập nhật danh mục: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // xoá category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") int categoryId){
        try{
            categoryService.deleteCategory(categoryId);
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Xóa danh mục thành công.");
            return ResponseEntity.ok().body(baseResponse) ;
        }
        catch(EntityNotFoundException e){
            System.out.println("Lỗi xóa danh mục: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(404, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch(Exception e){
            System.out.println("Lỗi xóa danh mục: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }


}
