package com.jpa.board.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.jpa.board.dto.BoardDTO;
import com.jpa.board.dto.FileDTO;
import com.jpa.board.dto.ReplyInterface;
import com.jpa.board.entity.BoardEntity;
import com.jpa.board.entity.FileEntity;

public interface BoardService {
	
    //게시물 목록 보기
	public Page<BoardEntity> list(String searchType, String keyword, int pageNum, int postNum);

	//게시물 내용 보기
	public BoardEntity view(Long seqno);
	
	// //게시물 내용 보기 중 이전버튼 값 가져 오기
	// public Long pre_seqno(Long seqno, String searchType, String keyword);
	
	// //게시물 내용 보기 중 다음버튼 값 가져 오기
	// public Long next_seqno(Long seqno, String searchType, String keyword);
	
	//첨부 파일 있는 게시물 등록할 때 사용하는 seqno 
	public Long getSeqnoWithLastNumber();
	
	//첨부 파일 없는 게시물 등록할 때 사용하는 seqno
	public Long getSeqnoWithNextval();
	
	//게시물 등록
	public void write(BoardDTO board);
	
	//게시물 등록 시 업로드된 파일 정보를 tbl_file에 등록
	public void fileInfoRegistry(FileDTO file);
	
	//게시물 수정
	public void modify(BoardDTO board);
	
	//게시물 수정 화면내의 파일 목록 가져 오기
	public List<FileEntity> fileListView(BoardEntity board);
	
	//게시물 수정 화면내의 파일 삭제
	public void deleteFileList(Long fileseqno);
	
	//게시물 삭제
	public void delete(Long seqno);

	//파일 다운로드를 위한 파일 정보 보기
	public FileEntity fileInfo(Long fileseqno);
	
	//댓글 보기
	public List<ReplyInterface> replyView(ReplyInterface reply);
	
	//댓글 수정
	public void replyUpdate(ReplyInterface reply);
	
	//댓글 등록 
	public void replyRegistry(ReplyInterface reply);
	
	//댓글 삭제
	public void replyDelete(ReplyInterface reply);
    
}
