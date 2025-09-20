package com.example.shopapp.controllers;

import com.example.domain.models.entities.Product;
import com.example.domain.services.IProductService;
import com.example.shopapp.pojos.PaginationInfo;
import com.example.shopapp.transfer.dtos.requests.ProductDTO;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import com.example.shopapp.transfer.dtos.responses.PagedResponse;
import com.example.shopapp.transfer.dtos.responses.ProductResponse;
import com.example.shopapp.transfer.mappers.ProductMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api-prefix}/products")
@Tag(name = "Product Management", description = "Quản lý hàng hóa trong hệ thống")
public class ProductController {

    private final IProductService productService;
    private final ProductMapper productMapper ;

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "18") int limit ,
            @RequestParam(required = false) String name,
            @RequestParam(required = false , value = "category_name") String categoryName,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false , value = "min_price") Float minPrice,
            @RequestParam(required = false , value = "max_price") Float maxPrice,
            @RequestParam(required = false , value = "min_stock") Integer minStock,
            @RequestParam(required = false , value = "min_rating") Float minRating,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, value ="min_views") Long minViews,
            @RequestParam(required = false) Integer status
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
            Page<Product> products;

            products = productService.filterProducts(
                    name,
                    categoryName,
                    color,
                    brand,
                    minPrice,
                    maxPrice,
                    minStock,
                    minRating,
                    description,
                    minViews,
                    status ,
                    pageRequest
            );
            // Xáo trộn danh sách sản phẩm
            List<Product> result = new ArrayList<>(products.getContent());
            Collections.shuffle(result);

            // Mapping sang response
            List<ProductResponse> productResponses = result.stream()
                    .map(productMapper::fromEntityToResponse)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    products.getNumber(),
                    products.getSize(),
                    products.getTotalPages(),
                    products.getTotalElements()
            );

            PagedResponse<ProductResponse> pagedResponse = new PagedResponse<>(productResponses, paginationInfo);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy danh sách sản phẩm thành công.", pagedResponse);

            return ResponseEntity.ok(baseResponse);

        } catch (Exception e) {
            System.err.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // lấy product theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductWithProductId(@PathVariable("id") int productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductResponse productResponse = productMapper.fromEntityToResponse(product) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy thông tin sản phẩm thành công.", productResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi lấy thông tin sản phẩm: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy thông tin sản phẩm: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // xem sản phẩm
    @GetMapping("/view/{id}")
    public ResponseEntity<?> viewProduct (@PathVariable("id") int productId){
        try{
            productService.viewProduct(productId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Xem sản phẩm thành công") ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (EntityNotFoundException e ) {
            System.out.println("Lỗi xem sản phẩm: " + productId);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (Exception e ) {
            System.out.println("Lỗi xem sản phẩm: " + productId);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // tạo sản phẩm mới
    @PostMapping()
    public ResponseEntity<?> createProduct(
            @RequestBody @Valid ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
               }
               System.out.println("Lỗi tạo sản phẩm: " + errorsBuilder);
               BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
           }
           Product product = productService.createProduct(productMapper.fromRequestToEntity(productDTO));
           ProductResponse productResponse = productMapper.fromEntityToResponse(product) ;
           BaseResponse baseResponse = BaseResponse.buildResponse("200", "Tạo sản phẩm thành công.", productResponse);
           return ResponseEntity.ok(baseResponse);
       } catch (Exception e) {
           System.out.println("Lỗi tạo sản phẩm: " + e.getMessage());
           BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
       }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createProducts(
            @RequestBody @Valid List<@Valid ProductDTO> productDTOs,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getObjectName())
                            .append("[")
                            .append(fieldError.getField())
                            .append("]: ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
               }
               System.out.println("Lỗi tạo sản phẩm: " + errorsBuilder);
               BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
           }
           List<Product> convertedProducts = productDTOs
                   .stream()
                   .map(productMapper :: fromRequestToEntity)
                   .toList() ;
           List<Product> products = productService.createProducts(convertedProducts);
           List<ProductResponse> productResponses = products
                   .stream()
                   .map(productMapper :: fromEntityToResponse)
                   .toList();
           BaseResponse baseResponse = BaseResponse.buildResponse("200", "Tạo sản phẩm thành công.",productResponses );
           return ResponseEntity.ok(baseResponse);

       } catch (Exception e) {
           System.out.println("Lỗi tạo sản phẩm: " + e.getMessage());
           BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
       }
    }


    //  cập nhật sản phẩm
    @PutMapping()
    public ResponseEntity<?> updateProduct(
            @RequestBody @Valid ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                StringBuilder errorsBuilder = new StringBuilder();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
               }
               System.out.println("Lỗi cập nhật sản phẩm: " + errorsBuilder);
               BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
           }
           Product product = productService.updateProduct(productMapper.fromRequestToEntity(productDTO));
           ProductResponse productResponse = productMapper.fromEntityToResponse(product) ;
           BaseResponse baseResponse = BaseResponse.buildResponse("200", "Cập nhật sản phẩm thành công.", productResponse);
           return ResponseEntity.ok(baseResponse);
       } catch (EntityNotFoundException e) {
           System.out.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
           BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
       } catch (Exception e) {
           System.out.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
           BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
       }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int productId) {
        try {
            productService.deleteProduct(productId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Xóa sản phẩm thành công.");
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi xóa sản phẩm: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi xóa sản phẩm: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
