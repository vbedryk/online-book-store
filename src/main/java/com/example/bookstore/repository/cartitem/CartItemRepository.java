package com.example.bookstore.repository.cartitem;

import com.example.bookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci JOIN ci.shoppingCart cs "
            + "WHERE ci.id = :id and cs.id = :shoppingCartId")
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);

    @Query("DELETE FROM CartItem ci WHERE ci.id = :itemId "
            + "AND ci.shoppingCart.id = :shoppingCartId")
    void deleteByIdAndShoppingCartId(Long itemId, Long shoppingCartId);
}
