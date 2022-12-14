package com.jpa.board.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="tbl_file")
public class FileEntity {
	
	@Id
	//@SequenceGenerator(name="FILE_SEQ", sequenceName="TBL_FILE_SEQ", initialValue=1, allocationSize=1)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="FILE_SEQ")
	private Long fileseqno;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="seqno")
	private BoardEntity seqno;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="userid")
	private MemberEntity userid;
	
	private String org_filename;
	private String stored_filename;
	private long filesize;
	private String checkfile;

}
