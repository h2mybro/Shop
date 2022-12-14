package com.jpa.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.jpa.board.constatnt.OrderStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_orders")
@Getter
@Setter
@Builder
public class OrderEntity {
    
    @Id
    @Column(name = "orderid")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid") // 한명의 유저는 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑을 한다.
    private MemberEntity member;

    @Column(name = "orderdate")
    private LocalDateTime orderDate; // 주문 날짜

    @Enumerated(EnumType.STRING)
    @Column(name = "orderstatus")
    private OrderStatus orderstatus; // 주문 상태 ORDER, CANCEL

    // 주문 상품 엔티티와 일대다 매핑을 한다. 외래키(orderid)가 orderitemEntity 테이블에 있으므로 연관 관계의 주인은 OrderItemEntity이다. OrderEntity가 주인이 아니므로
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, // mappedBy 속성으로 연관 관계의 주인을 설정한다. 속성값으로 order를 적어준 이유는
            orphanRemoval = true, fetch = FetchType.LAZY)  // OrderItemEntity에 있는 Order에 의해 관리된다는 의미. 즉 연관 관계의 주인의 필드인 order를 mappedBy의 값으로 세팅하면 된다.
            // orphanRemoval -> 고아객체 제거하는 설정 -> 부모객체가 사라지면 고아객체도 삭제된다
    private List<OrderItemEntity> orderItemEntities = new ArrayList<>(); // 하나의 주문이 여러 개의 주문 상품을 갖으므로 List 자료형을 사용해서 매핑한다.

}
