package com.example.domain.helpers;


import com.example.domain.models.entities.Product;
import com.example.domain.models.entities.ProductImage;
import com.example.domain.persistence.repositories.ProductImageRepository;
import com.example.domain.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileHelper {

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository ;

    private final String uploadDir = "product_images/";

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

        File directory = new File(uploadDir);
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

            Path filePath = Paths.get(uploadDir + fileName);
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

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
        return "";
    }

}
