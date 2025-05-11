package com.crepestrips.userservice.service;

import com.crepestrips.userservice.model.Cart;
import com.crepestrips.userservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Cacheable(value = "Carts", key = "#userId")
    public Optional<Cart> getCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    @CachePut(value = "Carts", key = "#result.userId")
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @CacheEvict(value = "Carts", key = "#userId")
    public void evictCartFromCache(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null.");
        }
        // Additional logic can be added here if needed, such as logging
        System.out.println("Cart evicted from cache for userId: " + userId);
    }
}