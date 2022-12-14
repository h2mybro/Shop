package com.jpa.board.entity;

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

@Entity
@Table(name="tbl_reply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyEntity {

	@Id
	@SequenceGenerator(name="reply_seq", sequenceName="tbl_reply_SEQUENCE", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="reply_seq")
	private Long replyseqno;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="seqno")
	private BoardEntity seqno;
	
	@Column(name="replywriter", length=50, nullable=false)
	private String replywriter;

	@Column(name="replycontent", length=2000, nullable=false)
	private String replycontent;
	
	@Column(name="replyregdate", nullable=false)
	private String replyregdate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="userid")
	private MemberEntity userid;
	
	public void updateReply(String replycontent) {
		this.replycontent = replycontent;
	}
	
}

