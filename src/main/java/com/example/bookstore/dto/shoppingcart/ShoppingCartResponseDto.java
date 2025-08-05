package com.example.bookstore.dto.shoppingcart;

import com.example.bookstore.dto.cartitem.CartItemDto;
import java.util.Set;

public record ShoppingCartResponseDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
) {
}
