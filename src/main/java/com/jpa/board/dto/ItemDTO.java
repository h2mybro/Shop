package com.jpa.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import com.jpa.board.constatnt.ItemSellStatus;
import com.jpa.board.entity.ItemEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemDTO {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDTO> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public ItemEntity createItem(){
        return modelMapper.map(this, ItemEntity.class);
    }

    public static ItemDTO of(ItemEntity itemEntity){
        return modelMapper.map(itemEntity,ItemDTO.class);
    }

}