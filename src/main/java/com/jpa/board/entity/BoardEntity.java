package com.jpa.board.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="tbl_board")
@AllArgsConstructor //전체 전역변수를 이용해서 생성자를 만들어 줌.
@NoArgsConstructor //예) A a = new A() --> 인자가 없는 생성자를 만들어 줌.  --> 기본 생성자
@Builder //Build 패턴 지원
public class BoardEntity {

    @Id
	// @SequenceGenerator(name="tbl_board_seq", sequenceName="tbl_board_sequence", initialValue = 1, allocationSize = 1) 
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="tbl_board_seq")
	@Column(name="seqno", nullable=false)
	private Long seqno;
	
	@Column(name="mwriter", length=50, nullable=true)
	private String mwriter;
		
	@Column(name="mtitle", length=200, nullable=true)
	private String mtitle;
	
	@Column(name="mregdate", nullable=true)
	private LocalDateTime mregdate;

	@Column(name="mcontent", length=2000, nullable=true)
	private String mcontent;
	
	//FK 만드는 법
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="userid")
	private MemberEntity userid;
	
	public void updateBoard(String mtitle,String mcontent) {
		
		this.mtitle = mtitle;
		this.mcontent = mcontent;
	
	}

}
