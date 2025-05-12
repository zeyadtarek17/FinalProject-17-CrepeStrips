package com.crepestrips.userservice.controller;

import com.crepestrips.userservice.model.Cart;
import com.crepestrips.userservice.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public Optional<Cart> getCartByUserId(@PathVariable UUID userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping
    public Cart saveCart(@Valid @RequestBody Cart cart) {
        return cartService.saveCart(cart);
    }
    @PostMapping("/create")
    public Cart createCart(@RequestParam UUID userId) {
        return cartService.createCart(userId);
    }

    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable UUID cartId) {
        return cartService.getCartById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
    }

    @DeleteMapping("/{cartId}")
    public String deleteCart(@PathVariable UUID cartId) {
        cartService.deleteCart(cartId);
        return "Cart deleted successfully.";
    }

}