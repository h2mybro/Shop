package com.jpa.board.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.jpa.board.dto.BoardDTO;
import com.jpa.board.dto.FileDTO;
import com.jpa.board.dto.ReplyInterface;
import com.jpa.board.entity.BoardEntity;
import com.jpa.board.entity.FileEntity;
import com.jpa.board.entity.MemberEntity;
import com.jpa.board.entity.ReplyEntity;
import com.jpa.board.entity.repository.BoardRepository;
import com.jpa.board.entity.repository.FileRepository;
import com.jpa.board.entity.repository.MemberRepository;
import com.jpa.board.entity.repository.ReplyRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@AllArgsConstructor
@Repository
@Service
public class BoardServiceImpl implements BoardService{

	private final BoardRepository boardRepository; // 생성자 방식으로 의존성을 주입해서 스프링빈을 불러내서 사용하는 것...
	private final FileRepository fileRepository;
	private final MemberRepository memberRepository;
	private final ReplyRepository replyRepository;
	
	//게시물 목록 보기
	@Override
	public Page<BoardEntity> list(String searchType, String keyword, int pageNum, int postNum){
		
		PageRequest pageRequest = PageRequest.of(pageNum-1,postNum,Sort.by(Direction.DESC, "seqno"));
		
		if(searchType.equals("mtitle")) 
			return boardRepository.findByMtitleContaining(keyword, pageRequest);
		else if(searchType.equals("mwriter"))
			return boardRepository.findByMwriterContaining(keyword, pageRequest);
		else if(searchType.equals("mcontent"))
			return boardRepository.findByMcontentContaining(keyword, pageRequest);
		else if(searchType.equals("mtitle_mcontent"))
			return boardRepository.findByMtitleContainingOrMcontentContaining(keyword, keyword, pageRequest);
		else return boardRepository.findAll(pageRequest);
	}

	//게시물 내용 보기
	@Override
	public BoardEntity view(Long seqno) {		
		log.info("boardServiceImpl view 도착!! ");
		return boardRepository.findById(seqno).get();

	}	

	//게시물 보기 및 수정 화면내의 파일 목록 가져 오기
	public List<FileEntity> fileListView(BoardEntity board){
			return fileRepository.findBySeqno(board);
	}
	
	//첨부 파일 있는 게시물 등록할 때 사용하는 seqno 
	@Override
	public Long getSeqnoWithLastNumber() {
		return boardRepository.getSeqnoWithLastNumber();
	}

	//첨부 파일 없는 게시물 등록할 때 사용하는 seqno 
	@Override
	public Long getSeqnoWithNextval() {
		return boardRepository.getSeqnoWithNextval();
	}

	//게시물 등록
	@Override
	public void write(BoardDTO board) {
		
		board.setMregdate(LocalDateTime.now());
		BoardEntity boardEntity = board.dtoToEntity(board);
		boardRepository.save(boardEntity);		
		
	}
	
	//게시물 등록 시 업로드된 파일 정보를 tbl_file에 등록
	@Override
	public void fileInfoRegistry(FileDTO fileDTO) {
		
		FileEntity fileEntity = fileDTO.dtoToEntity(fileDTO);
		fileRepository.save(fileEntity);
	}
	
	//게시물 수정
	public void modify(BoardDTO boardDTO) {
		
		BoardEntity boardEntity = boardRepository.findById(boardDTO.getSeqno()).get();
		boardEntity.updateBoard(boardDTO.getMtitle(), boardDTO.getMcontent());
		boardRepository.save(boardEntity);
	}
	
	//게시물 수정 화면내의 파일 삭제
	public void deleteFileList(Long fileseqno) {
		fileRepository.deleteById(fileseqno);		
	}
	
	//게시물 삭제
	public void delete(Long seqno) {
		BoardEntity boardEntity = boardRepository.findById(seqno).get();
		boardRepository.delete(boardEntity);
	}
	
	//파일 다운로드를 위한 파일 정보 보기
	@Override
	public FileEntity fileInfo(Long fileseqno) {
		return fileRepository.findById(fileseqno).get();		
	}

	// //게시물 내용 보기 중 이전버튼 값 가져 오기
	// @Override
	// public Long pre_seqno(Long seqno, String searchType, String keyword) {

	// 	Long pre_seqno = 0L;
	// 	//이전 버튼
	// 	if(searchType.equals("mtitle")) 
	// 		pre_seqno = boardRepository.findPreSeqnoByMtitle(seqno, keyword);
	// 	else if(searchType.equals("mwriter"))
	// 		pre_seqno = boardRepository.findPreSeqnoByMwriter(seqno, keyword);
	// 	else if(searchType.equals("mcontent"))
	// 		pre_seqno = boardRepository.findPreSeqnoByMcontent(seqno, keyword);
	// 	else if(searchType.equals("mcontent_mtitle"))
	// 		pre_seqno = boardRepository.findPreSeqnoByMtitleOrMcontent(seqno, keyword, keyword);
		
	// 	return pre_seqno;
	// }
	
	// //게시물 내용 보기 중 다음버튼 값 가져 오기
	// @Override
	// public Long next_seqno(Long seqno, String searchType, String keyword) {

	// 	Long next_seqno = 0L;
	// 	if(searchType.equals("mtitle")) 
	// 		next_seqno = boardRepository.findNextSeqnoByMtitle(seqno, keyword);
	// 	else if(searchType.equals("mwriter"))
	// 		next_seqno = boardRepository.findNextSeqnoByMwriter(seqno, keyword);
	// 	else if(searchType.equals("mcontent"))
	// 		next_seqno = boardRepository.findNextSeqnoByMcontent(seqno, keyword);
	// 	else if(searchType.equals("mcontent_mtitle"))
	// 		next_seqno = boardRepository.findNextSeqnoByMtitleOrMcontent(seqno, keyword, keyword);

	// 	return next_seqno;
	// }

	//댓글 보기
	@Override
	public List<ReplyInterface> replyView(ReplyInterface reply){
		
		return replyRepository.replyView(reply.getSeqno());
	}
	
	//댓글 수정
	@Override
	public void replyUpdate(ReplyInterface reply) {
		ReplyEntity replyEntity = replyRepository.findById(reply.getReplyseqno()).get();
		replyEntity.updateReply(reply.getReplycontent());
		replyRepository.save(replyEntity);
	}
	
	//댓글 등록 
	@Override
	public void replyRegistry(ReplyInterface reply) {
		
		BoardEntity boardEntity = boardRepository.findById(reply.getSeqno()).get();
		MemberEntity memberEntity = memberRepository.findById(reply.getUserid()).get();
		
		ReplyEntity replyEntity = ReplyEntity.builder()
								.replycontent(reply.getReplycontent())
								.replyregdate(LocalDateTime.now().toString())
								.replywriter(reply.getReplywriter())
								.seqno(boardEntity)
								.userid(memberEntity)
								.build();
								
		replyRepository.save(replyEntity);
	}
	
	//댓글 삭제
	@Override
	public void replyDelete(ReplyInterface reply) {
		ReplyEntity replyEntity = replyRepository.findById(reply.getReplyseqno()).get();
		replyRepository.delete(replyEntity);
	}
}
