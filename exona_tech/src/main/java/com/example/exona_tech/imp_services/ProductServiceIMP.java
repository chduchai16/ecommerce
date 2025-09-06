package com.example.exona_tech.imp_services;

import com.example.domain.dtos.requests.ProductDTO;
import com.example.domain.entities.Category;
import com.example.domain.entities.Product;
import com.example.domain.entities.Supplier;
import com.example.domain.repositories.CategoryRepository;
import com.example.domain.repositories.ProductRepository;
import com.example.domain.repositories.SupplierRepository;
import com.example.domain.services.IProductService;
import com.example.exona_tech.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceIMP implements IProductService {

    private final ProductRepository productRepository ;
    private final CategoryRepository categoryRepository ;
    private final SupplierRepository supplierRepository ;
    private final ProductMapper productMapper ;

    @Override
    public Product getProductById(int productId) throws Exception {
        return productRepository.findById(productId).orElseThrow(()->new Exception("Cannot find this product"));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable) ;
    }

    @Override
    public Product createProduct(Product product) throws Exception {
        return this.productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) throws Exception {
        if(product.getId() == null) {
            throw new Exception("Id must not be null to update");
        }
        else if (!this.productRepository.existsById(product.getId())){
            throw new Exception("This product does not exist");
        }
        return this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(int productId) throws Exception {
        if (productRepository.findById(productId).isEmpty()){
            throw new Exception("This product does not exist");
        }
        else {
            productRepository.deleteById(productId);
        }
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword.toLowerCase(), pageable);
    }

    @Override
    public Page<Product> getProductsByCategory(int categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public void viewProduct(int productId) throws Exception {
        if(productRepository.findById(productId).isEmpty()){
            throw new Exception("This product does not exist") ;
        }
        else {
            productRepository.viewProduct(productId) ;
        }
    }

    @Override
    public List<Product> getHotProducts(int limit) {
        return productRepository.getHotProducts(limit) ;
    }

    @Override
    public List<Product> getTop10BestSellingProducts() {
        LocalDate now = LocalDate.now();

        int month = now.getMonthValue();
        int year = now.getYear();

        return this.productRepository.getTop10HotestProduct(month , year) ;
    }

    @Override
    public List<Product> createProducts(List<Product> products) throws Exception {
        return this.productRepository.saveAll(products);
    }

}
