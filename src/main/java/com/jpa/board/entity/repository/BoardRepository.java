package com.jpa.board.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jpa.board.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>{
	
	// Optional<BoardEntity> findBySeqno(Long seqno);

    //게시물 목록 보기
	public Page<BoardEntity> findByMtitleContaining(String keyword, Pageable pageable);
	public Page<BoardEntity> findByMwriterContaining(String keyword, Pageable pageable);
	public Page<BoardEntity> findByMcontentContaining(String keyword, Pageable pageable);
	public Page<BoardEntity> findByMtitleContainingOrMcontentContaining(String keyword1, String keyword2, Pageable pageable);

	// //게시물 보기 이전 
	// @Query("select max(b.seqno) as preseqno from board b where b.seqno < :seqno and b.mtitle like %:keyword%")
	// Long findPreSeqnoByMtitle(@Param("seqno") Long seqno, @Param("keyword") String keyword);
	
	// @Query("select max(b.seqno) as preseqno from board b where b.seqno < :seqno and b.mwriter like %:keyword%")
	// Long findPreSeqnoByMwriter(@Param("seqno") Long seqno, @Param("keyword") String keyword);
	
	// @Query("select max(b.seqno) as preseqno from board b where b.seqno < :seqno and b.mcontent like %:keyword%")
	// Long findPreSeqnoByMcontent(@Param("seqno") Long seqno, @Param("keyword") String keyword);
	
	// @Query("select max(b.seqno) as preseqno from board b where b.seqno < :seqno and b.mcontent like %:keyword1% or b.mtitle like %:keyword2%")
	// Long findPreSeqnoByMtitleOrMcontent(@Param("seqno") Long seqno, @Param("keyword1") String keyword1, @Param("keyword2") String keyword2);

	// //게시물 보기 다음
	// @Query("select min(b.seqno) as preseqno from board b where b.seqno > :seqno and b.mtitle like %:keyword%")
	// Long findNextSeqnoByMtitle(@Param("seqno") Long seqno, @Param("keyword") String keyword);
	
	// @Query("select min(b.seqno) as preseqno from board b where b.seqno > :seqno and b.mwriter like %:keyword%")
	// Long findNextSeqnoByMwriter(@Param("seqno") Long seqno, @Param("keyword") String keyword);
	
	// @Query("select min(b.seqno) as preseqno from board b where b.seqno > :seqno and b.mcontent like %:keyword%")
	// Long findNextSeqnoByMcontent(@Param("seqno") Long seqno, @Param("keyword") String keyword);
	
	// @Query("select min(b.seqno) as preseqno from board b where b.seqno > :seqno and b.mcontent like %:keyword1% or b.mtitle like %:keyword2%")
	// Long findNextSeqnoByMtitleOrMcontent(@Param("seqno") Long seqno, @Param("keyword1") String keyword1, @Param("keyword2") String keyword2);

	
	//게시물 번호 구하기 - 시퀀스의 Last Number 사용 
	@Query(value="select (last_number - 1) as last_number from all_sequences where sequence_name = 'tbl_board_sequence' and sequence_owner = 'SPRINGDEV'", 
				nativeQuery= true)
	public Long getSeqnoWithLastNumber();

	//게시물 번호 구하기 - 시퀀스의 nextval 사용 
	@Query(value="select tbl_board_sequence.nextval as seqno from dual", nativeQuery= true)
	public Long getSeqnoWithNextval();
    
}
