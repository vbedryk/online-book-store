package com.example.bookstore.controller;

import com.example.bookstore.dto.order.OrderCreateRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderUpdateRequestDto;
import com.example.bookstore.dto.orderitem.OrderItemDto;
import com.example.bookstore.model.User;
import com.example.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API", description = "Operations related to orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Place a new order",
            description = "Allows a user to place an order based on their shopping cart")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto placeOrder(@RequestBody @Valid OrderCreateRequestDto orderRequestDto,
                                     Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.create(orderRequestDto, user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get order history",
            description = "Retrieves the authenticated user's past orders with pagination"
    )
    @GetMapping
    public Page<OrderDto> findAll(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update order status",
            description = "Allows an admin to update the status of an existing order"
    )
    @PatchMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Long orderId,
                             @RequestBody @Valid OrderUpdateRequestDto orderUpdateRequestDto) {
        orderService.updateStatus(orderId, orderUpdateRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get items of an order",
            description = "Retrieves all items in a specific order placed by the authenticated user"
    )
    @GetMapping("/{orderId}/items")
    public Page<OrderItemDto> findAllItems(@PathVariable Long orderId,
                                           Authentication authentication,
                                           Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllById(orderId, user.getId(), pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get specific order item",
            description = "Retrieves a specific item in"
                    + " a specific order placed by the authenticated user"
    )
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto findItem(@PathVariable Long orderId,
                                           @PathVariable Long itemId,
                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findItemById(orderId, itemId, user.getId());
    }
}
