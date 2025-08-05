package com.example.bookstore.controller;

import com.example.bookstore.dto.shoppingcart.CartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemUpdateDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.security.AuthenticationServiceImpl;
import com.example.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Show shopping cart",
            description = "Show shopping cart")
    @GetMapping
    public ShoppingCartResponseDto findShoppingCart(Authentication authentication) {
        return shoppingCartService.findShoppingCart(authenticationService.getCurrentUserId());
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book to shopping cart",
            description = "Add book to shopping cart")
    @PostMapping
    public ShoppingCartResponseDto addBookToShoppingCart(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto
    ) {
        return shoppingCartService.addBookToShoppingCart(cartItemRequestDto,
                authenticationService.getCurrentUserId());
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity",
            description = "Update quantity in shopping cart")
    @PutMapping("/items/{id}")
    public ShoppingCartResponseDto updateQuantity(
            @PathVariable Long id,
            @RequestBody CartItemUpdateDto cartItemUpdateDto
    ) {
        return shoppingCartService.updateItemQuantity(cartItemUpdateDto,
                id, authenticationService.getCurrentUserId());
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete book from shopping cart",
            description = "Delete book from shopping cart")
    @DeleteMapping("/items/{id}")
    public void deleteItemFromCart(@PathVariable Long id) {
        shoppingCartService.deleteItemFromCart(id, authenticationService.getCurrentUserId());
    }
}
