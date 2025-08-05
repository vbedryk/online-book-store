package com.example.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Positive;

public record CartItemRequestDto(
        @Positive
        Long bookId,
        @Positive
        Long quantity
) {
}
