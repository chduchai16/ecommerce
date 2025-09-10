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
import jakarta.persistence.EntityNotFoundException;
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
            BaseResponse baseResponse = BaseResponse.buildResponse("200","Lấy giỏ hàng thành công" , cartResponse);
            return ResponseEntity.ok(baseResponse) ;
        }
        catch (EntityNotFoundException e){
            System.out.println("Lỗi lấy giỏ hàng của người dùng:" + userId);
            BaseResponse baseResponse = BaseResponse.buildResponse("404" , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (Exception e){
            System.out.println("Lỗi lấy giỏ hàng của người dùng:" + userId);
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ.");
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
                System.out.println("Lỗi thêm sản phẩm vào giỏ hàng: " + errorBuilder);
                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Dữ liệu không hợp lệ.") ;
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
            }
            CartItem cartItem = cartItemMapper.fromRequestToEntity(cartItemDTO) ;
            Cart cart = cartService.addCartItemIntoCart(userId,cartItem) ;
            CartResponse cartResponse = cartMapper.fromEntityToResponse(cart) ;
            System.out.println("Thêm sản phẩm vào giỏ hàng thành công");
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Thêm sản phẩm vào giỏ hàng thành công", cartResponse) ;
            return ResponseEntity.ok(baseResponse) ;

        }
        catch (EntityNotFoundException e) {
            System.out.println("Lỗi thêm sản phẩm vào giỏ hàng");
            BaseResponse baseResponse = BaseResponse.buildResponse("404" , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch (Exception e) {
            System.out.println("Lỗi thêm sản phẩm vào giỏ hàng");
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " + e.getMessage());
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
                BaseResponse baseResponse = BaseResponse.buildResponse("400" , "Dữ liệu không hợp lệ" , error) ;
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse) ;
            }
            Cart cart = cartService.updateCart(cartMapper.fromRequestToEntity(cartDTO)) ;
            CartResponse cartResponse = cartMapper.fromEntityToResponse(cart) ;
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Cập nhật giỏ hàng thành công.",cartResponse) ;
            return ResponseEntity.ok(baseResponse) ;
        }
        catch(EntityNotFoundException e){
            System.out.println("Lỗi cập nhật giỏ hàng.");
            BaseResponse baseResponse = BaseResponse.buildResponse("404" , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
        catch(Exception exception){
            System.out.println("Lỗi cập nhật giỏ hàng.");
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " + exception.getMessage());
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
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Xóa sản phẩm khỏi giỏ hàng thành công.") ;
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse) ;
        }
        catch (EntityNotFoundException e) {
            System.out.println("Lỗi xóa sản phẩm khỏi giỏ hàng.");
            BaseResponse baseResponse = BaseResponse.buildResponse("404" , e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse) ;
        }
        catch (Exception exception) {
            System.out.println("Lỗi xóa sản phẩm khỏi giỏ hàng.");
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }

    // xoá tất cả item trong card theo card id
    @DeleteMapping("/{cart_id}/cart-items")
    public ResponseEntity<?> deleteAllItemsInCart(
            @PathVariable("cart_id") int cartId
    ){
        try{
            cartItemService.deleteCartItemsWithCartId(cartId);
            BaseResponse baseResponse = BaseResponse.buildResponse("200" , "Xóa tất cả sản phẩm trong giỏ hàng thành công.") ;
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse) ;
        }
        catch (EntityNotFoundException e){
            System.out.println("Lỗi xóa tất cả sản phẩm trong giỏ hàng: " + e.getMessage() );
            BaseResponse baseResponse = BaseResponse.buildResponse("404" , e.getMessage()) ;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse) ;
        }
        catch (Exception exception){
            System.out.println("Lỗi xóa tất cả sản phẩm trong giỏ hàng: " + exception.getMessage() );
            BaseResponse baseResponse = BaseResponse.buildResponse("500" , "Lỗi máy chủ nội bộ: " + exception.getMessage()) ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse) ;
        }
    }
}
