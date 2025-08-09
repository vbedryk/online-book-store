package com.example.bookstore.service.impl;

import com.example.bookstore.dto.order.OrderCreateRequestDto;
import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderUpdateRequestDto;
import com.example.bookstore.dto.orderitem.OrderItemDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.OrderProcessingException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.Status;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.order.OrderRepository;
import com.example.bookstore.repository.orderitem.OrderItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.repository.user.UserRepository;
import com.example.bookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto create(OrderCreateRequestDto orderRequestDto, Long currentUserId) {
        Order order = orderMapper.toModel(orderRequestDto);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find a shopping"
                        + " cart with user id " + currentUserId));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Shopping cart with user id: "
                    + currentUserId + " is empty");
        }
        order.setOrderDate(LocalDateTime.now());
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find a user"
                        + " with id " + currentUserId));
        order.setUser(user);
        order.setStatus(Status.PENDING);
        Set<OrderItem> orderItems = cartItemMapper.toOrderItem(shoppingCart.getCartItems());
        orderItems.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItems);
        order.setTotal(countTotalPrice(shoppingCart.getCartItems()));
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderDto> findAll(Long userId, Pageable pageable) {
        return (orderRepository.findOrdersByUserId(userId, pageable)
                .map(orderMapper::toDto));
    }

    @Override
    public OrderDto updateStatus(Long orderId, OrderUpdateRequestDto orderUpdateRequestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find a order"
                        + " with id " + orderId));
        order.setStatus(orderUpdateRequestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderItemDto> findAllById(Long orderId, Long userId, Pageable pageable) {
        return orderItemRepository.findAllByOrderUserIdAndOrderId(orderId, userId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemDto findItemById(Long orderId, Long itemId, Long userId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderIdAndOrderUserId(itemId,
                orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Cant find a order item"
                + " where order id " + orderId
                + " item id " + itemId
                + " user id " + userId));
        return orderItemMapper.toDto(orderItem);
    }

    private BigDecimal countTotalPrice(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(ci -> ci.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
