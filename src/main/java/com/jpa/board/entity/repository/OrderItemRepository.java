package com.jpa.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.board.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    
}
