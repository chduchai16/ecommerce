package com.example.shopapp.imp_services;

import com.example.domain.models.entities.Product;
import com.example.domain.models.enums.ProductStatus;
import com.example.domain.persistence.repositories.ProductRepository;
import com.example.domain.persistence.specifications.ProductSpecification;
import com.example.domain.services.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceIMP implements IProductService {

    private final ProductRepository productRepository ;

    // lấy thông tin
    @Override
    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("Không tìm thấy sản phẩm này"));
    }
    @Override
    public Page<Product> getHotProducts(Pageable pageable) {
        return productRepository.findAll(pageable) ;
    }

    @Override
    public Page<Product> filterProducts(
            String name,
            String categoryName,
            String color,
            String brand,
            Float minPrice,
            Float maxPrice,
            Integer minStock,
            Float minRating,
            String description,
            Long minViews,
            Pageable pageable
    ) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasCategoryName(categoryName))
                .and(ProductSpecification.hasColor(color))
                .and(ProductSpecification.hasBrand(brand))
                .and(ProductSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(ProductSpecification.hasStockQuantityGreaterThan(minStock))
                .and(ProductSpecification.hasAverageRatingGreaterThan(minRating))
                .and(ProductSpecification.hasDescription(description))
                .and(ProductSpecification.hasMoreViewsThan(minViews));

        return productRepository.findAll(spec, pageable);
    }

    // tạo mới
    @Override
    public Product createProduct(Product product){
        product.setStatus(ProductStatus.ACTIVE.ordinal());
        return this.productRepository.save(product);
    }

    @Override
    public List<Product> createProducts(List<Product> products){
        return this.productRepository.saveAll(products);
    }

    // cập nhật
    @Override
    public Product updateProduct(Product product) throws Exception {
        if(product.getId() == null) {
            throw new Exception("Id không được để trống khi cập nhật");
        }
        else if (!this.productRepository.existsById(product.getId())){
            throw new EntityNotFoundException("Sản phẩm này không tồn tại");
        }
        return this.productRepository.save(product);
    }

    @Override
    public void viewProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Sản phẩm này không tồn tại"));
        product.setViews(product.getViews() + 1);
        productRepository.save(product);
    }

    // xoá
    @Override
    public void deleteProduct(int productId) {
        if (productRepository.findById(productId).isEmpty()){
            throw new EntityNotFoundException("Sản phẩm này không tồn tại");
        }
        else {
            productRepository.deleteById(productId);
        }
    }
}
