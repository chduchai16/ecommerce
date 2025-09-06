package com.example.exona_tech.controllers;

import com.example.domain.dtos.requests.ProductDTO;
import com.example.domain.dtos.resposnes.BaseResponse;
import com.example.domain.dtos.resposnes.PagedResponse;
import com.example.domain.dtos.resposnes.ProductResponse;
import com.example.domain.entities.Product;
import com.example.domain.helpers.FileHelper;
import com.example.domain.pojos.PaginationInfo;
import com.example.domain.services.IProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api-prefix}/products")
@Tag(name = "Product Management", description = "Quản lý hàng hóa trong hệ thống")

public class ProductController {

    private final IProductService productService;
    private final FileHelper fileHelper;

    // tìm kiêm product theo keyword
    @GetMapping("/search")
    public ResponseEntity<?> getProductsWithKeyword(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "18") int limit,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
            Page<Product> products = productService.searchProducts(keyword, pageRequest);

            List<ProductResponse> productResponses = products.getContent()
                    .stream()
                    .map(ProductResponse::convertFromProduct)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    products.getNumber(),
                    products.getSize(),
                    products.getTotalPages(),
                    products.getTotalElements()
            );

            PagedResponse<ProductResponse> pagedResponse = new PagedResponse<>(productResponses, paginationInfo);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Search products successfully.", pagedResponse);

            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.err.println("Error searching products: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Search products failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // lấy product theo loại
    @GetMapping("/category")
    public ResponseEntity<?> getProductsWithCategoryId(
            @RequestParam(value = "id" , defaultValue = "1") int categoryId ,
            @RequestParam(value = "page", defaultValue = "0") int page ,
            @RequestParam(value = "limit", defaultValue = "18") int limit
    ){
        try{
            PageRequest pageRequest = PageRequest.of(page , limit ) ;
            Page<Product> products = productService.getProductsByCategory(categoryId , pageRequest) ;
            List<ProductResponse> productResponses = products.getContent().stream().map(ProductResponse::convertFromProduct).toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    products.getNumber() ,
                    products.getSize() ,
                    products.getTotalPages() ,
                    products.getTotalElements()
            ) ;

            PagedResponse pagedProductsResponse = new PagedResponse(productResponses , paginationInfo) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Search products successfully.", pagedProductsResponse);
            return ResponseEntity.ok(baseResponse);
        }
        catch (Exception e) {
            System.err.println("Error getting products: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "getting products failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // lấy product theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductWithProductId(@PathVariable("id") int productId) {
        try {
            Product product = productService.getProductById(productId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Get product successfully.", ProductResponse.convertFromProduct(product));
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error getting product: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get product failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // lấy danh sách phân trang cho sản phẩm mới
    @GetMapping()
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int limit
    ){
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
            Page<Product> products = productService.getAllProducts(pageRequest);

            List<Product> shuffled = new ArrayList<>(products.getContent());
            Collections.shuffle(shuffled); // Random tại đây

            PaginationInfo paginationInfo = new PaginationInfo(
                    products.getNumber(),
                    products.getSize(),
                    products.getTotalPages(),
                    products.getTotalElements()
            );

            List<ProductResponse> productResponses = shuffled.stream()
                    .map(ProductResponse::convertFromProduct)
                    .toList();

            PagedResponse<ProductResponse> pagedProductResponse = new PagedResponse<>(productResponses , paginationInfo) ;

            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Get all products successfully.",pagedProductResponse);
            return ResponseEntity.ok(baseResponse);

        } catch (Exception e) {
            System.out.println("Error getting all products: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get all products failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/hot")
    public ResponseEntity<?> getHotProducts(
            @RequestParam(defaultValue = "12") int limit
    ){
        try{
           List<Product> products = productService.getHotProducts(limit) ;
           List<ProductResponse> productResponses = products.stream().map(ProductResponse::convertFromProduct).toList() ;
           BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Get hot products successfully" , productResponses) ;
           return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e ) {
            System.out.println("Error get hot products");
            BaseResponse baseResponse = BaseResponse.buildResponse("500", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    // xem sản phẩm
    @GetMapping("/view/{id}")
    public ResponseEntity<?> viewProduct (@PathVariable("id") int productId){
        try{
            productService.viewProduct(productId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "View product successfully") ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e ) {
            System.out.println("Error view product: " + productId);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
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
                System.out.println("Error creating product: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Product product = productService.createProduct(productDTO);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Create product successfully.", ProductResponse.convertFromProduct(product));
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error creating product: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Create product failed.");
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
                System.out.println("Error creating products: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }

            List<Product> products = productService.createProducts(productDTOs);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Create products successfully.",
                    products.stream().map(ProductResponse::convertFromProduct).toList()
            );
            return ResponseEntity.ok(baseResponse);

        } catch (Exception e) {
            System.out.println("Error creating products: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Create products failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }


    // đọc ảnh
    @GetMapping("/images/{name}")
    public ResponseEntity<?> viewImage(
            @PathVariable("name") String fileName
    ){
        try {
            File file = new File("product_images/" + fileName);

            // Kiểm tra nếu tệp không tồn tại
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Lấy phần mở rộng của tệp từ tên ảnh
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

            // Xác định Content-Type dựa trên phần mở rộng
            String contentType;
            switch (extension) {
                case "jpg":
                case "jpeg":
                    contentType = MediaType.IMAGE_JPEG_VALUE;
                    break;
                case "png":
                    contentType = MediaType.IMAGE_PNG_VALUE;
                    break;
                case "webp":
                    contentType = "image/webp";
                    break;
                case "gif":
                    contentType = MediaType.IMAGE_GIF_VALUE;
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
            }

            // Đọc dữ liệu nhị phân của ảnh
            InputStream imageStream = new FileInputStream(file);
            byte[] imageBytes = imageStream.readAllBytes();
            imageStream.close();

            // Trả về ảnh với Content-Type phù hợp
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(imageBytes);
        }
        catch (Exception e) {
            System.out.println("View image failed: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "File does not exist.") ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // đăng ảnh
    @PostMapping("/uploads/{id}")
    public ResponseEntity<?> uploads(
            @PathVariable("id") int productId ,
            @RequestParam("file") MultipartFile[] imageFiles
    ){
        try {
            if (imageFiles.length == 0) {
                BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Upload product images successfully.") ;
                return ResponseEntity.ok(baseResponse) ;
            }
            String[] result = fileHelper.saveProductImage(productId , imageFiles);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Upload product images successfully." , result) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e) {
            System.out.println("Error uploading product images: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Upload product images failed.") ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }

    //  cập nhật sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @RequestBody @Valid ProductDTO productDTO,
            BindingResult result,
            @PathVariable("id") int productId
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
                System.out.println("Error updating product: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Product product = productService.updateProduct(productId, productDTO);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Update product successfully.", ProductResponse.convertFromProduct(product));
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Update product failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int productId) {
        try {
            productService.deleteProduct(productId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Delete product successfully.");
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error deleting product: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Delete product failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/top-10")
    public ResponseEntity<?> getTop10HotestProducts(){
        try {
            List<Product> products = productService.getTop10BestSellingProducts() ;
            List<ProductResponse> productResponses = products
                    .stream()
                    .map(product -> ProductResponse.convertFromProduct(product))
                    .toList();
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Get top 10 hotest product successfully." , productResponses) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(Exception exception) {
            System.out.println("Error get top 10 hotest products: " + exception.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Get products failed.") ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }
}
