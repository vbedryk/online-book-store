package com.example.bookstore.service;

import com.example.bookstore.dto.order.OrderCreateRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderUpdateRequestDto;
import com.example.bookstore.dto.orderitem.OrderItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto create(OrderCreateRequestDto orderRequestDto, Long userId);

    Page<OrderDto> findAll(Long userId, Pageable pageable);

    OrderDto updateStatus(Long orderId, OrderUpdateRequestDto orderUpdateRequestDto);

    Page<OrderItemDto> findAllById(Long orderId, Long userId, Pageable pageable);

    OrderItemDto findItemById(Long orderId, Long itemId, Long userid);
}
