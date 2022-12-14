package com.jpa.board.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "memberentity")
@Table(name = "tbl_member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MemberEntity {
    
    @Id // PK
    @Column(name = "userid", length = 100, nullable = false)
    private String userid;

    @Column(name = "userpw", length = 200, nullable = false)
    private String userpw;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name="telno", length=20, nullable=true)
	private String telno;
    
    @Column(name = "zipcode", length = 20, nullable = false)
    private String zipcode;
    
    @Column(name = "address", length = 200, nullable = false)
    private String address;
    
    @Column(name = "address2", length = 200, nullable = false)
    private String address2;

    @Column(name = "role", nullable = false)
	private String role;

    @Column(name="pwcheck", nullable=true)
	private int pwcheck;

    //사용자 정보 수정
	public void memberInfoUpdate(String zipcode, String address, String address2, String telno) {

        this.zipcode = zipcode;
        this.address = address;
        this.address2 = address2;
        this.telno = telno;

    }

	//사용자 패스워드 변경
	public void memberPasswordModify(String userpw) {
		
		this.userpw = userpw;
		this.pwcheck = 1;

	}

}
