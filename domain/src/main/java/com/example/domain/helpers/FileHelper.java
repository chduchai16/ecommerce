package com.example.domain.helpers;


import com.example.domain.models.entities.Product;
import com.example.domain.models.entities.ProductImage;
import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.ProductImageRepository;
import com.example.domain.persistence.repositories.ProductRepository;
import com.example.domain.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileHelper {

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository ;

    private final UserRepository userRepository;

    private final String productDir = "product_images/";
    private final String avatarDir = "user_avatars/";

    private static int MAXIMAGESPERPRODUCT = 5 ;

    public String[] saveProductImage(int productId, MultipartFile[] imageFiles) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("This product does not exist."));

        if (product.getProductImages().size() > MAXIMAGESPERPRODUCT) {
            throw new Exception("This product has enough images.");
        }

        if (imageFiles.length > MAXIMAGESPERPRODUCT) {
            throw new Exception("Cannot upload more than 5 images.");
        }

        if (product.getProductImages().size() + imageFiles.length > MAXIMAGESPERPRODUCT) {
            throw new Exception("Post excess photos for this product.");
        }

        String[] imageUrls = new String[imageFiles.length];

        File directory = new File(productDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (int i = 0; i < imageFiles.length; i++) {
            // Kiểm tra MIME type
            String mimeType = imageFiles[i].getContentType();
            if (mimeType == null || !mimeType.startsWith("image/")) {
                throw new Exception("Invalid image file: " + imageFiles[i].getOriginalFilename());
            }

            String fileExtension = getFileExtension(imageFiles[i].getOriginalFilename());
            String fileName = productId + "_" + UUID.randomUUID() + fileExtension;

            Path filePath = Paths.get(productDir + fileName);
            try {
                Files.write(filePath, imageFiles[i].getBytes());
            } catch (IOException e) {
                throw new Exception("Lỗi khi lưu ảnh: " + fileName, e);
            }

            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setImageName(fileName);
            productImageRepository.save(productImage);
            if(product.getProductImages().size() == 0) {
                product.setThumbnail(fileName);
            }
            product.getProductImages().add(productImage);
            imageUrls[i] = "/product_images/" + fileName;
        }

        productRepository.save(product);
        return imageUrls;
    }

    public String saveUserAvatar(int userId, MultipartFile file) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("Người dùng không tồn tại."));

        String mimeType = file.getContentType();
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new Exception("Tệp không phải ảnh: " + file.getOriginalFilename());
        }

        // Tạo thư mục nếu chưa có
        File dir = new File(avatarDir);
        if (!dir.exists()) dir.mkdirs();

        String ext = getFileExtension(file.getOriginalFilename());
        if (ext.isEmpty()) ext = ".png";
        String fileName = userId + "_" + UUID.randomUUID() + ext;
        Path outPath = Paths.get(avatarDir + fileName);

        try {
            Files.write(outPath, file.getBytes());
        } catch (IOException e) {
            throw new Exception("Lỗi khi lưu avatar: " + fileName, e);
        }

        // Cập nhật user
        user.setAvatar(fileName);
        userRepository.save(user);

        return fileName;
    }

    // Đọc file ảnh từ thư mục (trả về byte[]), ném Exception nếu không tìm thấy
    public byte[] readImageBytes(String dir, String fileName) throws Exception {
        Path path = Paths.get(dir, fileName);
        File file = path.toFile();
        if (!file.exists()) {
            throw new Exception("Tệp không tồn tại: " + path.toString());
        }
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new Exception("Lỗi khi đọc tệp: " + path.toString(), e);
        }
    }

    // Xác định Content-Type dựa trên phần mở rộng của file
    public String detectImageContentType(String fileName) throws Exception {
        String extension = getFileExtension(fileName).replaceFirst("\\.", "").toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
            case "png":
                return org.springframework.http.MediaType.IMAGE_PNG_VALUE;
            case "webp":
                return "image/webp";
            case "gif":
                return org.springframework.http.MediaType.IMAGE_GIF_VALUE;
            default:
                throw new Exception("Unsupported media type: " + extension);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
        return "";
    }

 }
