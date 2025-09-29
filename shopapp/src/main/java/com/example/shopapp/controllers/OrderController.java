package com.example.shopapp.controllers;

import com.example.domain.models.entities.Order;
import com.example.domain.services.IOrderService;
import com.example.shopapp.pojos.PaginationInfo;
import com.example.shopapp.transfer.dtos.requests.OrderDTO;
import com.example.shopapp.transfer.dtos.responses.BaseResponse;
import com.example.shopapp.transfer.dtos.responses.OrderResponse;
import com.example.shopapp.transfer.dtos.responses.PagedResponse;
import com.example.shopapp.transfer.mappers.OrderMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api-prefix}/orders")
@Tag(name = "Order Management", description = "Quản lý đơn hàng trong hệ thống")

public class OrderController {

    private final IOrderService orderService ;
    private final OrderMapper orderMapper ;

    // xem đơn hàng
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") int orderId){
        try {
            Order order = orderService.getOrderById(orderId);
            OrderResponse orderResponse = orderMapper.fromEntityToResponse(order);
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Lấy thông tin đơn hàng thành công." , orderResponse);
            return ResponseEntity.ok(baseResponse);
        }
        catch(EntityNotFoundException e) {
            System.out.println(String.format("Lỗi lấy thông tin đơn hàng(%s): %s", orderId, e.getMessage() ));
            BaseResponse baseResponse = BaseResponse.buildResponse(404 , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse) ;
        }
        catch(Exception e) {
            System.out.println(String.format("Lỗi lấy thông tin đơn hàng(%s): %s", orderId, e.getMessage() ));
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }

    // lấy danh dách phân trang tất cả đơn hàng
    @GetMapping()
    public ResponseEntity<?> filterOrders(
            @RequestParam(value ="minTotalAmount" , required = false) Float minTotalAmount ,
            @RequestParam(value ="maxTotalAmount" , required = false) Float maxTotalAmount ,
            @RequestParam(value ="status" , required = false) Integer status ,
            @RequestParam(value ="shippingAddress" , required = false) String shippingAddress ,
            @RequestParam(value ="customerName" , required = false) String customerName ,
            @RequestParam(value = "page" ,defaultValue = "0") int page ,
            @RequestParam(value = "limit" , defaultValue = "12") int limit
    ){
        try{
            PageRequest pageRequest = PageRequest.of(page , limit );

            Page<Order> orders = orderService.filterOrders(
                    minTotalAmount ,
                    maxTotalAmount ,
                    status ,
                    shippingAddress ,
                    customerName ,
                    pageRequest
            );

            List<OrderResponse> orderResponses = orders.getContent()
                    .stream()
                    .map(orderMapper :: fromEntityToResponse)
                    .toList() ;

            PaginationInfo paginationInfo = new PaginationInfo(
                    orders.getNumber() ,
                    orders.getSize() ,
                    orders.getTotalPages() ,
                    orders.getTotalElements()
            );

            PagedResponse pagedOrdersResponse = new PagedResponse(orderResponses , paginationInfo) ;

            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Lấy danh sách đơn hàng thành công." , pagedOrdersResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e){
            System.out.println("Lỗi lấy danh sách đơn hàng: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // lấy danh sách phân trang order của user theo id
    @GetMapping("/user")
    public ResponseEntity<?> getOrdersOfUser (
            @RequestParam("id") int userId,
            @RequestParam(value = "page" , defaultValue = "0") int page ,
            @RequestParam(value = "limit" , defaultValue = "5") int limit
    ){
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Order> orders = orderService.getOrdersByUserId(userId, pageRequest) ;
            List<OrderResponse> orderResponses = orders.getContent()
                    .stream()
                    .map(orderMapper :: fromEntityToResponse)
                    .toList() ;

            PaginationInfo paginationInfo = new PaginationInfo(
                    orders.getNumber() ,
                    orders.getSize() ,
                    orders.getTotalPages() ,
                    orders.getTotalElements()
            );

            PagedResponse pagedOrdersResponse  = new PagedResponse(orderResponses , paginationInfo);

            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Lấy danh sách đơn hàng thành công." , pagedOrdersResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e ) {
            System.out.println("Lỗi lấy danh sách đơn hàng: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500, "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // tạo mới đơn hàng (xem lại chỗ này)
    @PostMapping()
    public ResponseEntity<?> createOrder(
            @RequestBody @Valid OrderDTO orderDTO,
            BindingResult result
    ){
        try {
            if (result.hasErrors()){
                StringBuilder errorsBuilder = new StringBuilder() ;
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println("Lỗi dữ liệu đơn hàng: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse(400 , "Tạo đơn hàng thất bại.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Order order = orderService.createOrder(orderMapper.fromRequestToEntity(orderDTO));
            OrderResponse orderResponse = orderMapper.fromEntityToResponse(order);
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Tạo đơn hàng thành công." , orderResponse);
            return ResponseEntity.ok(baseResponse);
        }
        catch (Exception e ) {
            System.out.println("Lỗi tạo đơn hàng: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }

    // xem lại sau
//    // đặt hàng
//    @PostMapping("/place")
//    public ResponseEntity<?> placeOrder (
//            @RequestBody @Valid OrderDTO orderDTO,
//            BindingResult result
//    ){
//        try {
//            if(result.hasErrors()){
//                StringBuilder stringBuilder = new StringBuilder();
//                for(FieldError fieldError : result.getFieldErrors()){
//                    stringBuilder.append(fieldError).append("\n");
//                }
//                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Invalid data") ;
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
//            }
//            Order order = orderService.placeOrder(orderDTO) ;
//            OrderResponse orderResponse = OrderResponse.convertFromOrder(order);
//            BaseResponse baseResponse = BaseResponse.buildResponse("200","Place order successfully", orderResponse) ;
//            return ResponseEntity.status(HttpStatus.OK).body(baseResponse) ;
//
//        } catch (Exception exception) {
//            System.out.println("Error placing order: " + exception.getMessage());
//            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Place order failed: " + exception.getMessage()) ;
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
//        }
//    }

    // cạp nhật đơn hàng
    @PutMapping()
    public ResponseEntity<?> updateOrder(
            @RequestBody @Valid OrderDTO orderDTO,
            BindingResult result
    ){
        try {
            if (result.hasErrors()){
                StringBuilder errorsBuilder = new StringBuilder() ;
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorsBuilder.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n");
                }
                System.out.println("Lỗi dữ liệu đơn hàng: " + errorsBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse(400 , "Dữ liệu không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Order order = orderService.updateOrder(orderMapper.fromRequestToEntity(orderDTO));
            OrderResponse orderResponse  = orderMapper.fromEntityToResponse(order) ;
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Cập nhật đơn hàng thành công." , orderResponse);
            return ResponseEntity.ok(baseResponse);
        }
        catch (EntityNotFoundException e ) {
            System.out.println("Lỗi cập nhật đơn hàng: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(404 , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse) ;
        }
        catch (Exception e ) {
            System.out.println("Lỗi cập nhật đơn hàng: " + e.getMessage());
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }

    // xoá đơn hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable("id") int orderId
    ){
        try {
            orderService.deleteOrder(orderId);
            BaseResponse baseResponse = BaseResponse.buildResponse(200 , "Xóa đơn hàng thành công.");
            return ResponseEntity.ok(baseResponse);
        }
        catch (EntityNotFoundException e) {
            System.out.println(String.format("Lỗi xóa đơn hàng(%s): %s" , orderId,e.getMessage()));
            BaseResponse baseResponse = BaseResponse.buildResponse(404 , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse) ;
        }
        catch (Exception e) {
            System.out.println(String.format("Lỗi xóa đơn hàng(%s): %s" , orderId,e.getMessage()));
            BaseResponse baseResponse = BaseResponse.buildResponse(500 , "Lỗi máy chủ nội bộ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }
}
