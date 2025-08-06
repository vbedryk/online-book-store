package com.example.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Positive;

public record CartItemUpdateDto(
        @Positive
        int quantity
) {
}
