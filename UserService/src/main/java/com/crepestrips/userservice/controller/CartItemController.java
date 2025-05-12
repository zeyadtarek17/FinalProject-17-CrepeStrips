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

    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping("/{cartId}")
    public List<CartItem> getItemsByCartId(@PathVariable UUID cartId) {
        return cartItemService.getItemsByCartId(cartId);
    }

    @PostMapping
    public CartItem saveCartItem(@Valid @RequestBody CartItem cartItem) {
        return cartItemService.saveCartItem(cartItem);
    }
    @PostMapping("/add")
    public CartItem addItem(@RequestParam UUID cartId, @RequestBody CartItem item) {
        return cartItemService.addItemToCart(cartId, item);
    }

    @PutMapping("/{itemId}")
    public CartItem updateItem(@PathVariable UUID itemId, @RequestBody CartItem updatedItem) {
        return cartItemService.updateCartItem(itemId, updatedItem);
    }

    @DeleteMapping("/{itemId}")
    public String deleteItem(@PathVariable UUID itemId) {
        cartItemService.removeItem(itemId);
        return "Item removed from cart.";
    }

}