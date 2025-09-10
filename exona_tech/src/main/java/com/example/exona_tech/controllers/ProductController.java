package com.example.exona_tech.controllers;

import com.example.exona_tech.dtos.requests.ProductDTO;
import com.example.exona_tech.dtos.resposnes.BaseResponse;
import com.example.exona_tech.dtos.resposnes.PagedResponse;
import com.example.exona_tech.dtos.resposnes.ProductResponse;
import com.example.domain.entities.Product;
import com.example.domain.helpers.FileHelper;
import com.example.exona_tech.mappers.ProductMapper;
import com.example.exona_tech.pojos.PaginationInfo;
import com.example.domain.services.IProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final ProductMapper productMapper ;
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
                    .map(productMapper :: fromEntityToResponse)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    products.getNumber(),
                    products.getSize(),
                    products.getTotalPages(),
                    products.getTotalElements()
            );

            PagedResponse<ProductResponse> pagedResponse = new PagedResponse<>(productResponses, paginationInfo);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Tìm kiếm sản phẩm thành công.", pagedResponse);

            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.err.println("Lỗi tìm kiếm sản phẩm: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
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
            List<ProductResponse> productResponses = products.getContent()
                    .stream()
                    .map(productMapper :: fromEntityToResponse)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    products.getNumber() ,
                    products.getSize() ,
                    products.getTotalPages() ,
                    products.getTotalElements()
            ) ;

            PagedResponse pagedProductsResponse = new PagedResponse(productResponses , paginationInfo) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy danh sách sản phẩm thành công.", pagedProductsResponse);
            return ResponseEntity.ok(baseResponse);
        }
        catch (Exception e) {
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

            List<ProductResponse> productResponses = shuffled
                    .stream()
                    .map(productMapper :: fromEntityToResponse)
                    .toList();

            PagedResponse<ProductResponse> pagedProductResponse = new PagedResponse<>(productResponses , paginationInfo) ;

            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy danh sách sản phẩm thành công.",pagedProductResponse);
            return ResponseEntity.ok(baseResponse);

        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " +e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/hot")
    public ResponseEntity<?> getHotProducts(
            @RequestParam(defaultValue = "12") int limit
    ){
        try{
           List<Product> products = productService.getHotProducts(limit) ;
           List<ProductResponse> productResponses = products
                   .stream()
                   .map(productMapper :: fromEntityToResponse)
                   .toList() ;
           BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Lấy sản phẩm hot thành công" , productResponses) ;
           return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e ) {
            System.out.println("Lỗi lấy sản phẩm hot");
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
            System.out.println("Xem ảnh thất bại: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Tệp không tồn tại.") ;
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
                BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Tải lên ảnh sản phẩm thành công.") ;
                return ResponseEntity.ok(baseResponse) ;
            }
            String[] result = fileHelper.saveProductImage(productId , imageFiles);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Tải lên ảnh sản phẩm thành công." , result) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e) {
            System.out.println("Lỗi tải lên ảnh sản phẩm: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " +e.getMessage()) ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
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

    @GetMapping("/top-10")
    public ResponseEntity<?> getTop10HotestProducts(){
        try {
            List<Product> products = productService.getTop10BestSellingProducts() ;
            List<ProductResponse> productResponses = products
                    .stream()
                    .map(productMapper :: fromEntityToResponse)
                    .toList();
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Lấy 10 sản phẩm hot nhất thành công." , productResponses) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(Exception exception) {
            System.out.println("Lỗi lấy 10 sản phẩm hot nhất: " + exception.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " + exception.getMessage()) ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }
}
