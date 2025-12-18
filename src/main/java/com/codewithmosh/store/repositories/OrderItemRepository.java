package com.codewithmosh.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codewithmosh.store.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
