package com.crepestrips.userservice.service;

import com.crepestrips.userservice.model.CartItem;
import com.crepestrips.userservice.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getItemsByCartId(UUID cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }
}