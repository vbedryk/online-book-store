package com.example.bookstore.dto.orderitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
