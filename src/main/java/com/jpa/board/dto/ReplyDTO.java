package com.jpa.board.dto;

import com.jpa.board.entity.BoardEntity;
import com.jpa.board.entity.MemberEntity;
import com.jpa.board.entity.ReplyEntity;

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
public class ReplyDTO {

	private Long replyseqno;
	private BoardEntity seqno;
	private String replywriter;
	private String replycontent;
	private String replyregdate;
	private MemberEntity userid;
	
	public ReplyDTO(ReplyEntity entity) {
		
		this.replyseqno = entity.getReplyseqno();
		this.replywriter = entity.getReplywriter();
		this.replycontent = entity.getReplycontent();
		this.replyregdate = entity.getReplyregdate();
		this.userid = entity.getUserid();
		this.seqno = entity.getSeqno();
		
	}
	
	public ReplyEntity dtoToEntity(ReplyDTO dto) {
		
		ReplyEntity entity = ReplyEntity.builder()
							.userid(dto.getUserid())
							.seqno(dto.getSeqno())
							.replywriter(dto.getReplywriter())
							.replycontent(dto.getReplycontent())
							.replyregdate(dto.getReplyregdate())
							.build();
		return entity;
	}
	
}
