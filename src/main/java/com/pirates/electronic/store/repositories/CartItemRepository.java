package com.pirates.electronic.store.repositories;

import com.pirates.electronic.store.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,String> {
}
