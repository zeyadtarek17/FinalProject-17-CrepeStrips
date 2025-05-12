package com.crepestrips.userservice.service;
import com.crepestrips.userservice.model.CartItem;

import java.util.List;
import java.util.UUID;

public interface CartItemService {

    List<CartItem> getItemsByCartId(UUID cartId);

    CartItem getCartItemById(UUID itemId);

    CartItem saveCartItem(CartItem cartItem);

    CartItem updateCartItem(UUID itemId, CartItem updatedItem);

    void deleteCartItem(UUID itemId);

    CartItem addItemToCart(UUID cartId, CartItem item);

    void removeItem(UUID itemId);
}
