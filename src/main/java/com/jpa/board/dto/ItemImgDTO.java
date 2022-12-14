package com.jpa.board.dto;


import org.modelmapper.ModelMapper;

import com.jpa.board.entity.ItemImgEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemImgDTO {

    private Long id; 
    private String imgName; // 이미지 파일명
    private String oriImgName; // 원본 이미지 파일명
    private String imgUrl; // 이미지 조회 경로
    private String repImgYn; // 대표 이미지 여부

    private static ModelMapper modelMapper = new ModelMapper(); // 멤버 변수로 ModelMapper 객체 추가

    public static ItemImgDTO of(ItemImgEntity itemImgEntity) { // ItemImgEntitiy 객체를 파라미터로 받아서 ItemImgEntity 객체의 자료형과 멤버변수의 이름이 같을 때
        return modelMapper.map(itemImgEntity, ItemImgDTO.class); // ItemImgDTO로 값을 복사해서 반환한다. static 메소드로 선언해 ItemImgDTO 객체를 생성하지 않아도
                                                                                // 호출할 수 있도록한다.

    }

}