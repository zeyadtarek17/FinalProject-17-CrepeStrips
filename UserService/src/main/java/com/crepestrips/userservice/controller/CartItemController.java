package com.crepestrips.userservice.controller;

import com.crepestrips.userservice.model.CartItem;
import com.crepestrips.userservice.service.CartItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/{cartId}")
    public List<CartItem> getItemsByCartId(@PathVariable UUID cartId) {
        return cartItemService.getItemsByCartId(cartId);
    }

    @PostMapping
    public CartItem saveCartItem(@Valid @RequestBody CartItem cartItem) {
        return cartItemService.saveCartItem(cartItem);
    }
}