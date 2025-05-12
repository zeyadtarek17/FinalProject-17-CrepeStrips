package com.crepestrips.userservice.service;

import com.crepestrips.userservice.model.Cart;
import com.crepestrips.userservice.model.CartItem;
import com.crepestrips.userservice.repository.CartItemRepository;
import com.crepestrips.userservice.repository.CartRepository;
import com.crepestrips.userservice.service.CartItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> getItemsByCartId(UUID cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public CartItem getCartItemById(UUID itemId) {
        return cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + itemId));
    }

    @Override
    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(UUID itemId, CartItem updatedItem) {
        updatedItem.setId(itemId);
        return cartItemRepository.save(updatedItem);
    }

    @Override
    public void deleteCartItem(UUID itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Override
    public CartItem addItemToCart(UUID cartId, CartItem item) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
        item.setCart(cart);
        return cartItemRepository.save(item);
    }

    @Override
    public void removeItem(UUID itemId) {
        if (!cartItemRepository.existsById(itemId)) {
            throw new RuntimeException("Cart item not found with id: " + itemId);
        }
        cartItemRepository.deleteById(itemId);
    }
}
