package com.jpa.board.entity.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jpa.board.dto.ImgMapping;
import com.jpa.board.entity.ItemImgEntity;

@Repository
public interface ItemImgRepository extends JpaRepository<ItemImgEntity, Long> {

    List<ImgMapping> findAllBy();
    
    List<ItemImgEntity> findByIdOrderByIdAsc(Long itemImgId);    

}
