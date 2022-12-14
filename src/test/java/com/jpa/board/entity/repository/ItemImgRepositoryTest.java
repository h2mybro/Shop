package com.jpa.board.entity.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jpa.board.entity.ItemImgEntity;

@SpringBootTest
public class ItemImgRepositoryTest {


    @Autowired
    ItemImgRepository imgRepository;

    @Test
    List<ItemImgEntity> findByImgName(String imgName) {
        
        List<ItemImgEntity> itemImgEntities = findByImgName("87f83867-8499-42de-aae0-ae074576a90a.jpeg");

        System.out.println("ihihih");
        System.out.println(itemImgEntities);

        return itemImgEntities;

    }

}
