package com.pirates.electronic.store.repositories;

import com.pirates.electronic.store.entities.Order;
import com.pirates.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);
}
