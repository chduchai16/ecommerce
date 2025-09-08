package com.example.exona_tech.controllers;

import com.example.exona_tech.dtos.requests.SupplierDTO;
import com.example.exona_tech.dtos.resposnes.BaseResponse;
import com.example.exona_tech.dtos.resposnes.PagedResponse;
import com.example.exona_tech.dtos.resposnes.SupplierResponse;
import com.example.domain.entities.Supplier;
import com.example.exona_tech.mappers.SupplierMapper;
import com.example.exona_tech.pojos.PaginationInfo;
import com.example.domain.services.ISupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Get suppliers successfully.",pagedSuppliersResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error getting suppliers: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get suppliers failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplier(@PathVariable("id") int supplierId) {
        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            SupplierResponse supplierResponse = supplierMapper.fromEntityToResponse(supplier) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Get supplier successfully.",supplierResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error getting supplier: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Get supplier failed: " +e.getMessage());
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
                System.out.println("Error creating supplier: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid supplier data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Supplier supplier = supplierService.createSupplier(supplierMapper.fromRequestToEntity(supplierDTO));
            SupplierResponse supplierResponse = supplierMapper.fromEntityToResponse(supplier) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Create supplier successfully.", supplierResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error creating supplier: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Create supplier failed: " + e.getMessage());
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
                System.out.println("Error updating supplier: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400", "Invalid supplier data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            Supplier supplier = supplierService.updateSupplier(supplierMapper.fromRequestToEntity(supplierDTO));
            SupplierResponse supplierResponse = supplierMapper.fromEntityToResponse(supplier) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Update supplier successfully.", supplierResponse);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error updating supplier: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Update supplier failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable("id") int supplierId) {
        try {
            supplierService.deleteSupplier(supplierId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200", "Delete supplier successfully.");
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            System.out.println("Error deleting supplier: " + e);
            BaseResponse baseResponse = BaseResponse.buildResponse("500", "Delete supplier failed: " + e.getMessage() );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }
}
