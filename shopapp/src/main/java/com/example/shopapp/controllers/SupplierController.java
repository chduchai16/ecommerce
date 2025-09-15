package com.example.shopapp.controllers;

import com.example.domain.models.entities.Supplier;
import com.example.domain.services.ISupplierService;
import com.example.shopapp.pojos.PaginationInfo;
import com.example.shopapp.transfer.dtos.requests.SupplierDTO;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import com.example.shopapp.transfer.dtos.responses.PagedResponse;
import com.example.shopapp.transfer.dtos.responses.SupplierResponse;
import com.example.shopapp.transfer.mappers.SupplierMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api-prefix}/suppliers")
@Tag(name = "Supplier Management", description = "Quản lý nhà cung cấp trong hệ thống")

public class SupplierController {

    private final ISupplierService supplierService;
    private final SupplierMapper supplierMapper ;

    @GetMapping()
    public ResponseEntity<?> getAllSuppliers(
            @RequestParam("page") int page ,
            @RequestParam("limit") int limit
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page , limit) ;
            Page<Supplier> suppliers = supplierService.getAllSuppliers(pageRequest);
            List<SupplierResponse> supplierResponses = suppliers
                    .stream()
                    .map(supplierMapper :: fromEntityToResponse)
                    .toList();

            PaginationInfo paginationInfo = new PaginationInfo(
                    suppliers.getNumber() ,
                    suppliers.getSize() ,
                    suppliers.getTotalPages() ,
                    suppliers.getTotalElements()
            );

            PagedResponse pagedSuppliersResponse = new PagedResponse(supplierResponses , paginationInfo) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy danh sách nhà cung cấp thành công.",pagedSuppliersResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách nhà cung cấp: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplier(@PathVariable("id") int supplierId) {
        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            SupplierResponse supplierResponse = supplierMapper.fromEntityToResponse(supplier) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Lấy thông tin nhà cung cấp thành công.",supplierResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi lấy thông tin nhà cung cấp: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi lấy thông tin nhà cung cấp: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " +e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createSupplier(
            @RequestBody @Valid SupplierDTO supplierDTO,
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
                System.out.println("Lỗi tạo nhà cung cấp: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Supplier supplier = supplierService.createSupplier(supplierMapper.fromRequestToEntity(supplierDTO));
            SupplierResponse supplierResponse = supplierMapper.fromEntityToResponse(supplier) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Tạo nhà cung cấp thành công.", supplierResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi tạo nhà cung cấp: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateSupplier(
            @RequestBody @Valid SupplierDTO supplierDTO,
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
                System.out.println("Lỗi cập nhật nhà cung cấp: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Supplier supplier = supplierService.updateSupplier(supplierMapper.fromRequestToEntity(supplierDTO));
            SupplierResponse supplierResponse = supplierMapper.fromEntityToResponse(supplier) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Cập nhật nhà cung cấp thành công.", supplierResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi cập nhật nhà cung cấp: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật nhà cung cấp: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable("id") int supplierId) {
        try {
            supplierService.deleteSupplier(supplierId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Xóa nhà cung cấp thành công.");
            return ResponseEntity.ok(baseResponse);
        } catch (EntityNotFoundException e) {
            System.out.println("Lỗi xóa nhà cung cấp: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("404", e.getMessage() );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            System.out.println("Lỗi xóa nhà cung cấp: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Lỗi máy chủ nội bộ: " + e.getMessage() );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
