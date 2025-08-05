package com.example.bookstore.dto.user.mapper;

import com.example.bookstore.dto.shoppingCart.ShoppingCartResponseDTO;
import com.example.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDTO toDto(ShoppingCart shoppingCart);
}
