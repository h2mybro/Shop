package com.jpa.board.dto;

import com.jpa.board.entity.BoardEntity;
import com.jpa.board.entity.FileEntity;
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
public class FileDTO {

	private Long fileseqno;
	private BoardEntity seqno;
	private MemberEntity userid;
	private String org_filename;
	private String stored_filename;
	private long filesize;
	private String checkfile;
	
	public FileDTO(FileEntity entity) {
		
		this.fileseqno = entity.getFileseqno();
		this.seqno = entity.getSeqno();
		this.userid = entity.getUserid();
		this.org_filename = entity.getOrg_filename();
		this.stored_filename = entity.getStored_filename();
		this.filesize = entity.getFilesize();
		this.checkfile = entity.getCheckfile();		
		
	}
	
	public static FileEntity dtoToEntity(FileDTO dto) {
		
		FileEntity entity = FileEntity.builder()
							.userid(dto.getUserid())
							.seqno(dto.getSeqno())
							.org_filename(dto.getOrg_filename())
							.stored_filename(dto.getStored_filename())
							.filesize(dto.getFilesize())
							.checkfile(dto.getCheckfile())
							.build();
		return entity;
	}
	
}
