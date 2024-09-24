package com.pirates.electronic.store.services;

import com.pirates.electronic.store.dtos.AddItemToCartRequest;
import com.pirates.electronic.store.dtos.CartDto;

public interface CartService {

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    void removeItemFromCart(String userId, String cartItem);

    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}
