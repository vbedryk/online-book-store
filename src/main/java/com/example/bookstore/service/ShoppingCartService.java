package com.example.bookstore.service;

import com.example.bookstore.dto.shoppingcart.CartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemUpdateDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto findShoppingCart(Long currentUserId);

    ShoppingCartResponseDto addBookToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                  Long currentUserId);

    ShoppingCartResponseDto updateItemQuantity(CartItemUpdateDto cartItemUpdateDto,
                                               Long id,
                                               Long currentUserId);

    void deleteItemFromCart(Long id, Long currentUserId);

    public void createEmptyCart(User user);
}
