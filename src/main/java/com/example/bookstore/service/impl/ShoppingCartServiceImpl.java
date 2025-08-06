package com.example.bookstore.service.impl;

import com.example.bookstore.dto.shoppingcart.CartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemUpdateDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto findShoppingCart(Long currentUserId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find a shopping"
                        + " cart with user id " + currentUserId));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto addBookToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                         Long currentUserId) {
        Book book = bookRepository.findById(cartItemRequestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Cant find a book with id "
                        + cartItemRequestDto.bookId()));
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cant find a shopping cart with user id "
                                + currentUserId));
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();
        if (existingCartItem.isPresent()) {
            CartItem item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + cartItemRequestDto.quantity());
            cartItemRepository.save(item);
        } else {
            CartItem newCartItem = cartItemMapper.toModel(cartItemRequestDto);
            newCartItem.setShoppingCart(shoppingCart);
            newCartItem.setBook(book);
            CartItem savedItem = cartItemRepository.save(newCartItem);
            shoppingCart.getCartItems().add(savedItem);
        }
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartResponseDto updateItemQuantity(CartItemUpdateDto cartItemUpdateDto,
                                                      Long id,
                                                      Long currentUserId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find a shopping"
                        + " cart with user id " + currentUserId));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cant find a cart item"
                        + " with id " + id + " and shopping cart id " + shoppingCart.getId()));

        cartItem.setQuantity(cartItemUpdateDto.quantity());
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteItemFromCart(Long id, Long currentUserId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cant find shopping cart with user Id: " + currentUserId));
        cartItemRepository.deleteByIdAndShoppingCartId(id, shoppingCart.getId());
    }

    @Override
    public void createEmptyCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
