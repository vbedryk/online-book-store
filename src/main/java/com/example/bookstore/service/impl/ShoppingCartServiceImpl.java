package com.example.bookstore.service.impl;

import com.example.bookstore.dto.shoppingCart.ShoppingCartRequestDTO;
import com.example.bookstore.dto.shoppingCart.ShoppingCartResponseDTO;
import com.example.bookstore.dto.user.mapper.ShoppingCartMapper;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.shoppingCart.ShoppingCartRepository;
import com.example.bookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartResponseDTO findShoppingCart(Long currentUserId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find a shopping"
                        + " cart with user id " + currentUserId));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDTO addBookToShoppingCart(ShoppingCartRequestDTO shoppingCartRequestDTO,
                                                         Long currentUserId) {
        if (!bookRepository.existsById(shoppingCartRequestDTO.bookId())) {
            throw new EntityNotFoundException("Cant find a book"
                    + " with id " + shoppingCartRequestDTO.bookId());
        }
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find a shopping"
                        + " cart with user id " + currentUserId));

        return ;
    }
}
