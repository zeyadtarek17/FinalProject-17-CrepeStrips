package com.crepestrips.userservice.service;

import com.crepestrips.userservice.model.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartService {
    Optional<Cart> getCartByUserId(UUID userId);
    Optional<Cart> getCartById(UUID cartId);
    Cart saveCart(Cart cart);
    void deleteCart(UUID cartId);
    void evictCartFromCache(UUID userId);
    Cart createCart(UUID userId);
}
