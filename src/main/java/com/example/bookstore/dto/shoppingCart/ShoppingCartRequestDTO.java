package com.example.bookstore.dto.shoppingCart;

import jakarta.validation.constraints.Positive;

public record ShoppingCartRequestDTO(
        @Positive
        Long bookId,
        @Positive
        Long quantity
) {
}
