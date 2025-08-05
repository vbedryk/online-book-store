package com.example.bookstore.dto.shoppingCart;

import com.example.bookstore.model.CartItem;

import java.util.Set;

public record ShoppingCartResponseDTO (
        Long id,
        Long userId,
        Set<CartItem> cartItems
) {
}
