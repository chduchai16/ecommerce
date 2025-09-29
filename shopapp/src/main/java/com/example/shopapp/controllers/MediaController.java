package com.example.shopapp.controllers;


import com.example.domain.helpers.FileHelper;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Tệp không tồn tại.") ;
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
}
