package com.jpa.board.dto;

import org.modelmapper.ModelMapper;

import com.jpa.board.entity.ItemEntity;
import com.jpa.board.entity.ItemImgEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDTO {
    
    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    private static ModelMapper modelMapper = new ModelMapper(); // 멤버 변수로 ModelMapper 객체 추가

    public static ItemImgDTO of(ItemImgEntity itemImgEntity) { // ItemImgEntitiy 객체를 파라미터로 받아서 ItemImgEntity 객체의 자료형과 멤버변수의 이름이 같을 때
        return modelMapper.map(itemImgEntity, ItemImgDTO.class); // ItemImgDTO로 값을 복사해서 반환한다. static 메소드로 선언해 ItemImgDTO 객체를 생성하지 않아도
                                                                                // 호출할 수 있도록한다.

    }

    public static ItemDTO of(ItemEntity itemEntity){
        return modelMapper.map(itemEntity,ItemDTO.class);
        
    }

}
