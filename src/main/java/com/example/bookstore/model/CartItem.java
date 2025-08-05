package com.example.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "shopping_cart", nullable = false)
    private ShoppingCart shoppingCart;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "book", nullable = false)
    private Book book;
    @NotNull
    private Integer quantity;
}
