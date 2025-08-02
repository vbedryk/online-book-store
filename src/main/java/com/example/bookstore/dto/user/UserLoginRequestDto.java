package com.example.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @NotBlank @Length(min = 10, max = 30) @Email String email,
        @NotBlank @Length(min = 8, max = 20) String password
) {
}
