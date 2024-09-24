package com.pirates.electronic.store.controllers;

import com.pirates.electronic.store.dtos.AddItemToCartRequest;
import com.pirates.electronic.store.dtos.ApiResponseMessage;
import com.pirates.electronic.store.dtos.CartDto;
import com.pirates.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    //add items to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request, @PathVariable String userId){
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    //remove item from the cart
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable String itemId){
        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Item is removed").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //clear cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Cart is Empty !!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //get cart
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId){
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }

}
