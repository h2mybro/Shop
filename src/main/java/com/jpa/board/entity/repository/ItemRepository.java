package com.jpa.board.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.board.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    Page<ItemEntity> findByItemNmContaining(String keyword, Pageable pageable);

}
