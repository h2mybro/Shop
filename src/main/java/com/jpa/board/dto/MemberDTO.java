package com.jpa.board.dto;

import com.jpa.board.entity.MemberEntity;

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
public class MemberDTO {
    
    private String userid; // PK
    private String userpw;
    private String username;
    private String telno;
    private String zipcode;
    private String address;
    private String address2;
    private String role;
    private int pwcheck;

    public MemberEntity dtoToEntity(MemberDTO dto) {
        
        MemberEntity entity = MemberEntity.builder()
                            .userid(dto.getUserid())
                            .userpw(dto.getUserpw())
                            .username(dto.getUsername())
                            .telno(dto.getTelno())
                            .zipcode(dto.getZipcode())
                            .address(dto.getAddress())
                            .address2(dto.getAddress2())
                            .role(dto.getRole())
                            .pwcheck(dto.getPwcheck())
                            .build();
        
        return entity;

    }


    public MemberDTO(MemberEntity entity) {

        this.userid = entity.getUserid();
        this.userpw = entity.getUserpw();
        this.username = entity.getUsername();
        this.telno = entity.getTelno();
        this.zipcode = entity.getZipcode();
        this.address = entity.getAddress();
        this.address2 = entity.getAddress2();
		this.role = entity.getRole();
        this.pwcheck = entity.getPwcheck();


    }

}
