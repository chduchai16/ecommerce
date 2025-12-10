package com.example.shopapp.services;

import com.example.shopapp.models.enums.InventoryStats;

import com.example.shopapp.models.entities.InventoryHistory;
import com.example.shopapp.models.entities.OrderDetail;
import com.example.shopapp.models.entities.Product;
import com.example.shopapp.models.entities.User;
import com.example.shopapp.repositories.InventoryHistoryRepository;
import com.example.shopapp.repositories.OrderDetailRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.specifications.InventoryHistorySpecification;
import com.example.shopapp.specifications.OrderDetailSpecification;
import com.example.shopapp.specifications.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryHistoryServiceIMP implements IInventoryService {
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final InventoryHistoryRepository inventoryHistoryRepository;

    private static final Integer WARNING_THRESHOLD = 10;
    private static final Integer CRITICAL_THRESHOLD = 5;

    @Override
    public Page<Product> getSellerInventory(
            Integer sellerId,
            String search,
            Integer status,
            PageRequest pageRequest
    ) throws Exception {
        try {
            Specification<Product> spec = Specification.where(
                    ProductSpecification.hasSellerId(sellerId)
            );

            if (search != null && !search.isEmpty()) {
                spec = spec.and(ProductSpecification.hasName(search));
            }

            if (status != null) {
                spec = spec.and(ProductSpecification.hasStatus(status));
            }

            return productRepository.findAll(spec, pageRequest);
        } catch (Exception e) {
            System.out.println("Lỗi lấy kho hàng của seller: " + e.getMessage());
            throw new Exception("Lỗi lấy kho hàng: " + e.getMessage());
        }
    }

    @Override
    public InventoryStats getSellerInventoryStats(Integer sellerId) throws Exception {
        try {
            Specification<Product> spec = Specification.where(
                    ProductSpecification.hasSellerId(sellerId)
            );

            List<Product> products = productRepository.findAll(spec);

            int totalProducts = products.size();
            int totalStock = products.stream()
                    .mapToInt(Product::getStockQuantity)
                    .sum();

            int warningCount = (int) products.stream()
                    .filter(p -> p.getStockQuantity() < WARNING_THRESHOLD)
                    .count();

            return InventoryStats.builder()
                    .totalProducts(totalProducts)
                    .totalStock(totalStock)
                    .warningCount(warningCount)
                    .build();
        } catch (Exception e) {
            System.out.println("Lỗi lấy thống kê kho hàng: " + e.getMessage());
            throw new Exception("Lỗi lấy thống kê: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Product importStock(
            Integer productId,
            Integer quantity,
            String note,
            User performedBy
    ) throws Exception {
        try {
            // Validate
            if (quantity <= 0) {
                throw new Exception("Số lượng nhập phải lớn hơn 0");
            }

            // Tìm sản phẩm
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new EntityNotFoundException("Sản phẩm không tồn tại");
            }

            Product product = productOpt.get();
            Integer quantityBefore = product.getStockQuantity();
            Integer quantityAfter = quantityBefore + quantity;

            // Cập nhật tồn kho
            product.setStockQuantity(quantityAfter);
            product.setInStock(quantityAfter > 0);
            Product savedProduct = productRepository.save(product);

            // Lưu lịch sử
            InventoryHistory history = InventoryHistory.builder()
                    .product(product)
                    .action("IMPORT")
                    .quantityBefore(quantityBefore)
                    .quantityAfter(quantityAfter)
                    .quantityChange(quantity)
                    .performedBy(performedBy)
                    .reason("Nhập kho")
                    .note(note)
                    .performedAt(LocalDateTime.now())
                    .build();

            inventoryHistoryRepository.save(history);

            System.out.println(String.format("Nhập kho sản phẩm %s: +%d (từ %d -> %d)",
                    product.getName(), quantity, quantityBefore, quantityAfter));

            return savedProduct;
        } catch (Exception e) {
            System.out.println("Lỗi nhập kho: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Product updateProductStock(
            Integer productId,
            Integer newQuantity,
            User performedBy,
            String reason
    ) throws Exception {
        try {
            // Validate
            if (newQuantity < 0) {
                throw new Exception("Số lượng tồn kho không được âm");
            }

            // Tìm sản phẩm
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new EntityNotFoundException("Sản phẩm không tồn tại");
            }

            Product product = productOpt.get();
            Integer quantityBefore = product.getStockQuantity();
            Integer quantityChange = newQuantity - quantityBefore;

            // Cập nhật tồn kho
            product.setStockQuantity(newQuantity);
            product.setInStock(newQuantity > 0);
            Product savedProduct = productRepository.save(product);

            // Lưu lịch sử
            InventoryHistory history = InventoryHistory.builder()
                    .product(product)
                    .action("ADJUST")
                    .quantityBefore(quantityBefore)
                    .quantityAfter(newQuantity)
                    .quantityChange(quantityChange)
                    .performedBy(performedBy)
                    .reason(reason != null ? reason : "Điều chỉnh kho")
                    .performedAt(LocalDateTime.now())
                    .build();

            inventoryHistoryRepository.save(history);

            System.out.println(String.format("Cập nhật kho sản phẩm %s: %s%d (từ %d -> %d)",
                    product.getName(), quantityChange >= 0 ? "+" : "", quantityChange, quantityBefore, newQuantity));

            return savedProduct;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật tồn kho: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Product exportStock(
            Integer productId,
            Integer quantity,
            String reason,
            User performedBy
    ) throws Exception {
        try {
            // Validate
            if (quantity <= 0) {
                throw new Exception("Số lượng xuất phải lớn hơn 0");
            }

            // Tìm sản phẩm
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new EntityNotFoundException("Sản phẩm không tồn tại");
            }

            Product product = productOpt.get();
            Integer quantityBefore = product.getStockQuantity();

            // Kiểm tra tồn kho đủ
            if (quantityBefore < quantity) {
                throw new Exception(String.format(
                        "Số lượng tồn kho không đủ. Yêu cầu: %d, Hiện có: %d",
                        quantity, quantityBefore
                ));
            }

            Integer quantityAfter = quantityBefore - quantity;

            // Cập nhật tồn kho
            product.setStockQuantity(quantityAfter);
            product.setInStock(quantityAfter > 0);
            Product savedProduct = productRepository.save(product);

            // Lưu lịch sử
            InventoryHistory history = InventoryHistory.builder()
                    .product(product)
                    .action("EXPORT")
                    .quantityBefore(quantityBefore)
                    .quantityAfter(quantityAfter)
                    .quantityChange(-quantity)
                    .performedBy(performedBy)
                    .reason(reason != null ? reason : "Xuất kho")
                    .performedAt(LocalDateTime.now())
                    .build();

            inventoryHistoryRepository.save(history);

            System.out.println(String.format("Xuất kho sản phẩm %s: -%d (từ %d -> %d)",
                    product.getName(), quantity, quantityBefore, quantityAfter));

            return savedProduct;
        } catch (Exception e) {
            System.out.println("Lỗi xuất kho: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Long getProductTotalSold(Integer productId) {
        try {
            Specification<OrderDetail> spec = Specification.where(
                    OrderDetailSpecification.hasProductId(productId)
            );

            List<OrderDetail> orderDetails = orderDetailRepository.findAll(spec);
            return orderDetails.stream()
                    .mapToLong(OrderDetail::getNumberOfProducts)
                    .sum();
        } catch (Exception e) {
            System.out.println("Lỗi tính tổng đã bán: " + e.getMessage());
            return 0L;
        }
    }

    @Override
    public Page<InventoryHistory> getSellerInventoryHistory(
            Integer sellerId,
            PageRequest pageRequest
    ) throws Exception {
        try {
            Specification<InventoryHistory> spec = Specification.where(
                    InventoryHistorySpecification.hasSellerId(sellerId)
            );

            return inventoryHistoryRepository.findAll(spec, pageRequest);
        } catch (Exception e) {
            System.out.println("Lỗi lấy lịch sử kho: " + e.getMessage());
            throw new Exception("Lỗi lấy lịch sử kho: " + e.getMessage());
        }
    }

    @Override
    public List<InventoryHistory> getProductHistory(Integer productId) throws Exception {
        try {
            Specification<InventoryHistory> spec = Specification.where(
                    InventoryHistorySpecification.hasProductId(productId)
            );

            return inventoryHistoryRepository.findAll(spec);
        } catch (Exception e) {
            System.out.println("Lỗi lấy lịch sử sản phẩm: " + e.getMessage());
            throw new Exception("Lỗi lấy lịch sử sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public Page<InventoryHistory> getSellerImportHistory(
            Integer sellerId,
            PageRequest pageRequest
    ) throws Exception {
        try {
            Specification<InventoryHistory> spec = Specification.where(
                    InventoryHistorySpecification.hasSellerIdAndAction(sellerId, "IMPORT")
            );

            return inventoryHistoryRepository.findAll(spec, pageRequest);
        } catch (Exception e) {
            System.out.println("Lỗi lấy lịch sử nhập kho: " + e.getMessage());
            throw new Exception("Lỗi lấy lịch sử nhập kho: " + e.getMessage());
        }
    }

    @Override
    public Boolean isWarningStock(Integer productId) throws Exception {
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new EntityNotFoundException("Sản phẩm không tồn tại");
            }

            Product product = productOpt.get();
            return product.getStockQuantity() < WARNING_THRESHOLD;
        } catch (Exception e) {
            System.out.println("Lỗi kiểm tra cảnh báo: " + e.getMessage());
            throw e;
        }
    }
}

