package com.example.bookstore.controller;

import com.example.bookstore.dto.shoppingCart.CartItemUpdateDto;
import com.example.bookstore.dto.shoppingCart.ShoppingCartRequestDTO;
import com.example.bookstore.dto.shoppingCart.ShoppingCartResponseDTO;
import com.example.bookstore.security.AuthenticationServiceImpl;
import com.example.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart API", description = "Operations related to carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final AuthenticationServiceImpl authenticationService;
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartResponseDTO findShoppingCart(Authentication authentication) {
        return shoppingCartService.findShoppingCart(authenticationService.getCurrentUserId());
    }

    @PostMapping
    public ShoppingCartResponseDTO addBookToShoppingCart(
            @RequestBody @Valid ShoppingCartRequestDTO shoppingCartRequestDTO
    ) {
        return shoppingCartService.addBookToShoppingCart(shoppingCartRequestDTO,
                authenticationService.getCurrentUserId());
    }

    @PutMapping("/items/{id}")
    public ShoppingCartResponseDTO updateQuantity(
            @PathVariable Long id,
            @RequestBody CartItemUpdateDto cartItemUpdateDto
    ) {
        return null;
    }

    @DeleteMapping("/items/{id}")
    public void deleteBookFromCart(@PathVariable Long id) {

    }
}
