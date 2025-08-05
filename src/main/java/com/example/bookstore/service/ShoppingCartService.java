package com.example.bookstore.service;

import com.example.bookstore.dto.shoppingCart.ShoppingCartRequestDTO;
import com.example.bookstore.dto.shoppingCart.ShoppingCartResponseDTO;

public interface ShoppingCartService {
    ShoppingCartResponseDTO findShoppingCart(Long currentUserId);

    ShoppingCartResponseDTO addBookToShoppingCart(ShoppingCartRequestDTO shoppingCartRequestDTO,
                                                  Long currentUserId);
}
