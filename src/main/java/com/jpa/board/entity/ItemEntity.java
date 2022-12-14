package com.jpa.board.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import com.jpa.board.constatnt.ItemSellStatus;
import com.jpa.board.dto.ItemDTO;

@Entity
@Table(name="tbl_item")
@Getter
@Setter
@ToString
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(name="price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    public void updateItem(ItemDTO itemDTO){
        this.itemNm = itemDTO.getItemNm();
        this.price = itemDTO.getPrice();
        this.stockNumber = itemDTO.getStockNumber();
        this.itemDetail = itemDTO.getItemDetail();
        this.itemSellStatus = itemDTO.getItemSellStatus();

    }

    // public void removeStock(int stockNumber){
    //     int restStock = this.stockNumber - stockNumber;
    //     if(restStock<0){
    //         throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
    //     }
    //     this.stockNumber = restStock;
    // }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }

}