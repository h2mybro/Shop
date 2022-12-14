package com.jpa.board.dto;

import java.time.LocalDateTime;

import com.jpa.board.entity.BoardEntity;
import com.jpa.board.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

    private Long seqno;
	private String mwriter;
	private String mtitle;
	private LocalDateTime mregdate;
	private String mcontent;
	private MemberEntity userid;


    public BoardEntity dtoToEntity(BoardDTO dto) {
		
		BoardEntity entity = BoardEntity.builder()
							.seqno(dto.getSeqno())
							.mwriter(dto.getMwriter())
							.mtitle(dto.getMtitle())
							.mregdate(dto.getMregdate())
							.mcontent(dto.getMcontent())
							.userid(dto.getUserid())
							.build();
		return entity;
	}
	
	public BoardDTO(BoardEntity entity) {
    	
    	this.seqno = entity.getSeqno();
    	this.mtitle = entity.getMtitle();
    	this.mcontent = entity.getMcontent();
    	this.mwriter = entity.getMwriter();
    	this.mregdate = entity.getMregdate();
    	this.userid = entity.getUserid();
	}

    
    


}
