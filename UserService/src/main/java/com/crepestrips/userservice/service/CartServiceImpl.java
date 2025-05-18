package com.crepestrips.userservice.service;

import com.crepestrips.userservice.model.Cart;
import com.crepestrips.userservice.repository.CartRepository;
import com.crepestrips.userservice.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    @Cacheable(value = "Carts", key = "#userId")
    public Optional<Cart> getCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Optional<Cart> getCartById(UUID cartId) {
        return cartRepository.findById(cartId);
    }

    @Override
    @CachePut(value = "Carts", key = "#result.userId")
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(UUID cartId) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        cartOpt.ifPresent(cart -> evictCartFromCache(cart.getUserId()));
        cartRepository.deleteById(cartId);
    }
    @Override
    @CachePut(value = "Carts", key = "#result.userId")
    public Cart createCart(UUID userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepository.save(cart);
    }

    @Override
    @CacheEvict(value = "Carts", key = "#userId")
    public void evictCartFromCache(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null.");
        }
        System.out.println("Cart evicted from cache for userId: " + userId);
    }
}
