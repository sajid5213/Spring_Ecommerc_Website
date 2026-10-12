package com.codewithmosh.store.repositories;

import java.util.UUID;
import com.codewithmosh.store.entities.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, UUID> {

}