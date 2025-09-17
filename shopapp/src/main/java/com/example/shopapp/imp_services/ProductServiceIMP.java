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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceIMP implements IProductService {

    private final ProductRepository productRepository ;

    // lấy thông tin
    @Override
    public Product getProductById(int productId) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasId(productId)) ;
        Optional<Product> product = productRepository.findOne(spec);
        if (product.isEmpty()) {
            throw new EntityNotFoundException("Sản phẩm này không tồn tại");
        }
        return product.get();
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
            Integer status ,
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
                .and(ProductSpecification.hasMoreViewsThan(minViews))
                .and(ProductSpecification.hasStatus(status));
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
        Specification<Product> spec = Specification.where(ProductSpecification.hasId(productId)) ;
        Optional<Product> product = productRepository.findOne(spec);
        if (product.isEmpty()) {
            throw new EntityNotFoundException("Sản phẩm này không tồn tại");
        }
        product.get().setViews(product.get().getViews() + 1);
        productRepository.save(product.get());
    }

    // xoá
    @Override
    public void deleteProduct(int productId) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasId(productId)) ;
        Optional<Product> product = productRepository.findOne(spec);
        if (product.isEmpty()) {
            throw new EntityNotFoundException("Sản phẩm này không tồn tại");
        }
        productRepository.deleteById(productId);
    }
}
