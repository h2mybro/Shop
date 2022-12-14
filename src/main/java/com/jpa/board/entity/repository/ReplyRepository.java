package com.jpa.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.board.dto.ReplyInterface;
import com.jpa.board.entity.BoardEntity;
import com.jpa.board.entity.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity,Long>{
	
	public List<ReplyEntity> findBySeqno(BoardEntity board);
	
	@Query(value="select replyseqno,replywriter,replycontent,replyregdate,seqno,userid from tbl_reply where seqno=:seqno order by replyseqno desc",nativeQuery= true)
	List<ReplyInterface> replyView(@Param("seqno") Long seqno);

}
