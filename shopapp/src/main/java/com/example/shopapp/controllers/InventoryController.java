package com.example.shopapp.controllers;

import com.example.domain.models.entities.InventoryHistory;
import com.example.domain.models.entities.User;
import com.example.domain.services.IInventoryService;
import com.example.shopapp.pojos.PaginationInfo;
import com.example.shopapp.transfer.dtos.requests.InventoryImportDTO;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import com.example.shopapp.transfer.dtos.responses.InventoryHistoryResponse;
import com.example.shopapp.transfer.dtos.responses.InventoryResponse;
import com.example.shopapp.transfer.dtos.responses.PagedResponse;
import com.example.shopapp.transfer.mappers.InventoryHistoryMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api-prefix}/inventory")
@Tag(name = "Inventory Management", description = "Quản lý tồn kho sản phẩm")
public class InventoryController {

    private final IInventoryService inventoryService;
    private final InventoryHistoryMapper inventoryHistoryMapper;

    @GetMapping("/seller/my-products")
    public ResponseEntity<?> getSellerInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer status,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.buildResponse(401, "Lỗi", "Vui lòng đăng nhập"));
            }

            Integer sellerId = currentUser.getId();
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());

            var inventory = inventoryService.getSellerInventory(sellerId, search, status, pageRequest);

            List<InventoryResponse> inventoryResponses = inventory.getContent().stream()
                    .map(product -> InventoryResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(product.getPrice())
                            .stockQuantity(product.getStockQuantity())
                            .brand(product.getBrand())
                            .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                            .build())
                    .collect(Collectors.toList());

            PagedResponse<InventoryResponse> pagedResponse = new PagedResponse<>(
                    inventoryResponses,
                    new PaginationInfo(
                            page,
                            limit,
                            inventory.getTotalPages(),
                            inventory.getTotalElements()
                    )
            );

            return ResponseEntity.ok(BaseResponse.buildResponse(200, "Thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
        }
    }

    @PostMapping("/import")
    public ResponseEntity<?> importStock(
            @Valid @RequestBody InventoryImportDTO importDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errors = new ArrayList<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.add(error.getDefaultMessage());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.buildResponse(400, "Lỗi validation", String.join(", ", errors)));
            }

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.buildResponse(401, "Lỗi", "Vui lòng đăng nhập"));
            }

            var product = inventoryService.importStock(
                    importDTO.getProductId(),
                    importDTO.getQuantity(),
                    importDTO.getNote(),
                    currentUser
            );

            InventoryResponse response = InventoryResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .stockQuantity(product.getStockQuantity())
                    .brand(product.getBrand())
                    .build();

            return ResponseEntity.ok(BaseResponse.buildResponse(200, "Thành công", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
        }
    }

    @PostMapping("/export")
    public ResponseEntity<?> exportStock(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.buildResponse(401, "Lỗi", "Vui lòng đăng nhập"));
            }

            var product = inventoryService.exportStock(
                    productId,
                    quantity,
                    reason,
                    currentUser
            );

            InventoryResponse response = InventoryResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .stockQuantity(product.getStockQuantity())
                    .brand(product.getBrand())
                    .build();

            return ResponseEntity.ok(BaseResponse.buildResponse(200, "Thành công", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
        }
    }

    @PutMapping("/adjust")
    public ResponseEntity<?> updateProductStock(
            @RequestParam Integer productId,
            @RequestParam Integer newQuantity,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.buildResponse(401, "Lỗi", "Vui lòng đăng nhập"));
            }

            var product = inventoryService.updateProductStock(
                    productId,
                    newQuantity,
                    currentUser,
                    reason
            );

            InventoryResponse response = InventoryResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .stockQuantity(product.getStockQuantity())
                    .brand(product.getBrand())
                    .build();

            return ResponseEntity.ok(BaseResponse.buildResponse(200, "Thành công", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
        }
    }

    @GetMapping("/seller/history")
    public ResponseEntity<?> getSellerInventoryHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.buildResponse(401, "Lỗi", "Vui lòng đăng nhập"));
            }

            Integer sellerId = currentUser.getId();
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("performedAt").descending());

            var history = inventoryService.getSellerInventoryHistory(sellerId, pageRequest);

            List<InventoryHistoryResponse> historyResponses = history.getContent().stream()
                    .map(inventoryHistoryMapper::fromEntityToResponse)
                    .collect(Collectors.toList());

            PagedResponse<InventoryHistoryResponse> pagedResponse = new PagedResponse<>(
                    historyResponses,
                    new PaginationInfo(
                            page,
                            limit,
                            history.getTotalPages(),
                            history.getTotalElements()
                    )
            );

            return ResponseEntity.ok(BaseResponse.buildResponse(200, "Thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
        }
    }


    @GetMapping("/seller/import-history")
    public ResponseEntity<?> getSellerImportHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.buildResponse(401, "Lỗi", "Vui lòng đăng nhập"));
            }

            Integer sellerId = currentUser.getId();
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("performedAt").descending());

            var importHistory = inventoryService.getSellerImportHistory(sellerId, pageRequest);

            List<InventoryHistoryResponse> historyResponses = importHistory.getContent().stream()
                    .map(inventoryHistoryMapper::fromEntityToResponse)
                    .collect(Collectors.toList());

            PagedResponse<InventoryHistoryResponse> pagedResponse = new PagedResponse<>(
                    historyResponses,
                    new PaginationInfo(
                            page,
                            limit,
                            importHistory.getTotalPages(),
                            importHistory.getTotalElements()
                    )
            );

            return ResponseEntity.ok(BaseResponse.buildResponse(200, "Thành công", pagedResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
        }
    }


    @GetMapping("/product/{productId}/history")
    public ResponseEntity<?> getProductHistory(
            @PathVariable Integer productId
    ) {
        try {
            List<InventoryHistory> histories = inventoryService.getProductHistory(productId);

            List<InventoryHistoryResponse> historyResponses = histories.stream()
                    .map(inventoryHistoryMapper::fromEntityToResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(BaseResponse.buildResponse(200, "Thành công", historyResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
        }
    }


    @GetMapping("/warning/{productId}")
    public ResponseEntity<?> checkWarningStock(@PathVariable Integer productId) {
        try {
            Boolean isWarning = inventoryService.isWarningStock(productId);
            return ResponseEntity.ok(BaseResponse.buildResponse(200, "Thành công", isWarning));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
        }
    }

    /**
     * Lấy doanh thu của seller
     */
    @GetMapping("/seller/revenue")
    public ResponseEntity<?> getSellerRevenue(
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.buildResponse(401, "Lỗi", "Vui lòng đăng nhập"));
            }

            Integer sellerId = currentUser.getId();
            var revenue = inventoryService.getSellerRevenue(sellerId);

            return ResponseEntity.ok(BaseResponse.buildResponse(200, "Thành công", revenue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
}
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)

                    .body(BaseResponse.buildResponse(500, "Lỗi", e.getMessage()));
