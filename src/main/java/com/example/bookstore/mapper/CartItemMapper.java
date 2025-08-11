package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.shoppingcart.CartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemUpdateDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.OrderItem;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "book", source = "bookId", qualifiedByName = "mapBookIdToBook")
    CartItem toModel(CartItemRequestDto dto);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    void updateFromDto(CartItemUpdateDto dto, @MappingTarget CartItem cartItem);

    @Named("mapBookIdToBook")
    default Book mapBookIdToBook(Long bookId) {
        if (bookId == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookId);
        return book;
    }

    default Set<OrderItem> toOrderItem(Set<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Can't map empty cart items to order items");
        }
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
