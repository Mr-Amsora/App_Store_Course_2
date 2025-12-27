package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<List<Cart>> getCartsByUser(User user);
}