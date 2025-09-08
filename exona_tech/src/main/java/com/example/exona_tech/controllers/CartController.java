package com.example.exona_tech.controllers;

import com.example.domain.entities.CartItem;
import com.example.exona_tech.dtos.requests.CartDTO;
import com.example.exona_tech.dtos.requests.CartItemDTO;
import com.example.exona_tech.dtos.resposnes.BaseResponse;
import com.example.exona_tech.dtos.resposnes.CartResponse;
import com.example.domain.entities.Cart;
import com.example.domain.services.ICartItemService;
import com.example.domain.services.ICartService;
import com.example.exona_tech.mappers.CartItemMapper;
import com.example.exona_tech.mappers.CartMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api-prefix}/carts")
@RequiredArgsConstructor
@Tag(name = "Cart Management", description = "Quản lý giỏ hàng của người dùng")

public class CartController {

    private final ICartService cartService ;
    private final ICartItemService cartItemService ;
    private final CartMapper cartMapper ;
    private final CartItemMapper cartItemMapper ;

    // lấy thông tin cart cho người dunng
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getCart(
            @PathVariable("id") int userId
    ){
        try{
            Cart cart = cartService.getCartByUserId(userId) ;
            CartResponse cartResponse =  cartMapper.fromEntityToResponse(cart);
            BaseResponse baseResponse = BaseResponse.buildResponse("200","Get cart successfully" , cartResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (Exception e){
            System.out.println("Error getting cart user:" + userId);
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Get cart failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // thêm item vào cart cho nguoi dung theo id ngươ dùng
    @PostMapping("/user/{id}/item")
    public ResponseEntity<?> addItem (
            @PathVariable("id") int userId ,
            @RequestBody CartItemDTO cartItemDTO,
            BindingResult result
    ){
        try{
            if(result.hasErrors()){
                StringBuilder errorBuilder = new StringBuilder() ;
                for(FieldError x : result.getFieldErrors()){
                    errorBuilder.append(x).append("\n") ;
                }
                System.out.println("Error add item into cart: " + errorBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Invalid data.") ;
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            CartItem cartItem = cartItemMapper.fromRequestToEntity(cartItemDTO) ;
            Cart cart = cartService.addCartItemIntoCart(userId,cartItem) ;
            CartResponse cartResponse = cartMapper.fromEntityToResponse(cart) ;
            System.out.println("Add item into cart successfully");
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Add item into cart successfully", cartResponse) ;
            return ResponseEntity.ok(baseResponse) ;

        }
        catch (Exception e) {
            System.out.println("Error add item into cart");
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Get cart failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // cập nhật cart
    @PutMapping()
    public ResponseEntity<?> updateCart(
            @RequestBody @Valid CartDTO cartDTO,
            BindingResult result
    ){
        try{
            if(result.hasErrors()){
                StringBuilder errorBuilder = new StringBuilder() ;
                for(FieldError fieldError : result.getFieldErrors()){
                    errorBuilder.append(fieldError).append("\n");
                }
                String error = errorBuilder.toString() ;
                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Invalid data" , error) ;
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Cart cart = cartService.updateCart(cartMapper.fromRequestToEntity(cartDTO)) ;
            CartResponse cartResponse = cartMapper.fromEntityToResponse(cart) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Update cart successfully.",cartResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(Exception exception){
            System.out.println("Error updating cart.");
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Update cart failed: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // xoá cart item cho cart theo id
    @DeleteMapping("/{cart_id}/cart-items/{cart_items_id}")
    public ResponseEntity<?> deleteCartItem(
            @PathVariable("cart_id") int cartId,
            @PathVariable("cart_items_id") int cartItemId
    ){
        try{
            cartItemService.deleteCartItem(cartId , cartItemId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Delete cart item successfully.") ;
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse) ;
        }
        catch (Exception exception) {
            System.out.println("Error deleting cart item.");
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Delete cart item faled.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
        }
    }

    // xoá tất cả item trong card theo card id
    @DeleteMapping("/{cart_id}/cart-items")
    public ResponseEntity<?> deleteAllItemsInCart(
            @PathVariable("cart_id") int cartId
    ){
        try{
            cartItemService.deleteCartItemsWithCartId(cartId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Delete cart items successfully.") ;
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse) ;
        }
        catch (Exception exception){
            System.out.println("Error deleting all cart items: " + exception.getMessage() );
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Delete cart items faild: " + exception.getMessage()) ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }
}
