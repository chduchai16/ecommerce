package com.example.shopapp.controllers;


import com.example.domain.helpers.FileHelper;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api-prefix}/media")
@Tag(name = "Media Management", description = "Quản lý media trong hệ thống")

public class MediaController {
    private final FileHelper fileHelper;


    // đọc ảnh
    @GetMapping("/images/{name}")
    public ResponseEntity<?> viewImage(
            @PathVariable("name") String fileName
    ){
        try {
            // Sử dụng FileHelper để đọc bytes và xác định content type
            byte[] imageBytes = fileHelper.readImageBytes("product_images", fileName);
            String contentType = fileHelper.detectImageContentType(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(imageBytes);
        }
        catch (Exception e) {
            System.out.println("Xem ảnh thất bại: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Tệp không tồn tại.") ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // đọc avatar người dùng từ thư mục user_avatars
    @GetMapping("/images/users/{name}")
    public ResponseEntity<?> viewUserAvatar(@PathVariable("name") String fileName) {
        try {
            byte[] imageBytes = fileHelper.readImageBytes("user_avatars", fileName);
            String contentType = fileHelper.detectImageContentType(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(imageBytes);
        } catch (Exception e) {
            System.out.println("Xem avatar thất bại: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Tệp không tồn tại.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // đăng ảnh
    @PostMapping("/uploads/products/{id}")
    public ResponseEntity<?> uploads(
            @PathVariable("id") int productId ,
            @RequestParam("file") MultipartFile[] imageFiles
    ){
        try {
            if (imageFiles.length == 0) {
                BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Tải lên ảnh sản phẩm thành công.") ;
                return ResponseEntity.ok(baseResponse) ;
            }
            String[] result = fileHelper.saveProductImage(productId , imageFiles);
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Tải lên ảnh sản phẩm thành công." , result) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e) {
            System.out.println("Lỗi tải lên ảnh sản phẩm: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " +e.getMessage()) ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }

    // đăng avatar cho người dùng
    @PostMapping("/uploads/users/{id}")
    public ResponseEntity<?> uploadUserAvatar(@PathVariable("id") int userId,
                                              @RequestParam("file") MultipartFile file) {
        try {
            String avatarUrl = fileHelper.saveUserAvatar(userId, file);
            BaseResponse baseResponse = BaseResponse.buildResponse(200, "Tải lên avatar thành công.", avatarUrl);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi tải lên avatar: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
