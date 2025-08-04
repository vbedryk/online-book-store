package com.example.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank
        String name,
        @NotBlank
        String description
) {
}
