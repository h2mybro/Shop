package com.jpa.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jpa.board.dto.BoardDTO;
import com.jpa.board.dto.FileDTO;
import com.jpa.board.dto.ReplyInterface;
import com.jpa.board.entity.BoardEntity;
import com.jpa.board.entity.FileEntity;
import com.jpa.board.entity.MemberEntity;
import com.jpa.board.service.BoardService;
import com.jpa.board.service.MemberService;
import com.jpa.board.util.PageUtil;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@Controller
public class BoardController {

private final BoardService service; //의존성 주입
private final MemberService memberService;

//게시물 목록 보기
@GetMapping("/board/list")
public void GetList(Model model, @RequestParam(name="page", defaultValue = "1") int pageNum, 
		@RequestParam(name="searchType", defaultValue="mtitle", required=false) String searchType, 
		@RequestParam(name="keyword", defaultValue="", required=false) String keyword ) throws Exception{

	int listCount = 5; //페이지 리스트에 보여질 페이지 갯수
	int postNum = 5; //한 페이지에 보여질 게시물 행의 갯수
	
	Page<BoardEntity> list = service.list(searchType, keyword, pageNum, postNum);

	PageUtil pageUtil = new PageUtil();
			
	int totalCount = (int)list.getTotalElements();
	
	model.addAttribute("list", service.list(searchType,keyword,pageNum,postNum));
	model.addAttribute("page", pageNum);
	model.addAttribute("searchType", searchType);
	model.addAttribute("keyword", keyword);
	model.addAttribute("pageListView", pageUtil.getPageList(pageNum, postNum, listCount, totalCount, searchType, keyword));
	
}

//게시판 내용 보기
@GetMapping("/board/view")
public void getView(Model model, @RequestParam(name="seqno", required=false) Long seqno, 
		@RequestParam(value="searchType", defaultValue="mtitle", required=false) String searchType,
		@RequestParam(value="keyword", defaultValue="", required=false) String keyword,
		@RequestParam(value="page", defaultValue="1", required=false) int pageNum,
		HttpSession session) throws Exception {
	
	BoardEntity view = service.view(seqno);
	String userid = (String)session.getAttribute("userid");
	
	model.addAttribute("view", view);
	model.addAttribute("viewUserid", view.getUserid().getUserid());
	model.addAttribute("fileListView", service.fileListView(view));
	model.addAttribute("pageNum", pageNum);
	model.addAttribute("searchType", searchType);
	model.addAttribute("keyword", keyword);
	// model.addAttribute("pre_seqno", service.pre_seqno(seqno, searchType, keyword));
	// model.addAttribute("next_seqno", service.next_seqno(seqno, searchType, keyword));
	
}

//게시물 등록 화면 보기
@GetMapping("/board/write")
public void GetWrite() { }


//첨부 파일 있는 게시물 등록
@PostMapping("/board/writeWithFile")
public String PostWriteWithFile(BoardDTO board, HttpServletRequest request) throws Exception{

	log.info("<-------------- 첨부 파일 있음 ------------------->");

	Long seqno = service.getSeqnoWithLastNumber();
	log.info("board.seqno = ", seqno);

	// board.setSeqno(seqno);

	log.info("board.seqno 왜 안와?");

	// service.modify(board);
	
	return "redirect:/board/list?page=1";
}

//첨부 파일 없는 게시물 등록
@PostMapping("/board/write")
public String PostWrite(BoardDTO board) throws Exception{
	
	log.info("<-------------- 첨부 파일 없음 ------------------->");
	Long seqno = service.getSeqnoWithNextval();
	board.setSeqno(seqno);
	
	service.write(board);
	
	return "redirect:/board/list?page=1";
}

//파일 업로드
@ResponseBody
@PostMapping("/board/fileUpload")
public void postFileUpload(@RequestParam("SendToFileList") List<MultipartFile> multipartFile, 
		@RequestParam("kind") String kind,@RequestParam(name="seqno", defaultValue="0", required=false) Long seqno,
		HttpSession session) throws Exception{
	
	log.info("파일 전송...");
	String path = "/Users/yy_ehh/Desktop/Springboot/ChooWarShop/boardfile/"; 
	String userid = (String)session.getAttribute("userid");
	String username = (String)session.getAttribute("username");
	
	if(kind.equals("I")) { 
		seqno = service.getSeqnoWithNextval();
		
		MemberEntity memberEntity = memberService.memberInfo(userid);
		BoardDTO boardDTO = BoardDTO.builder()
				.seqno(seqno)
				.mwriter(username)
				.mregdate(LocalDateTime.now())
				.userid(memberEntity)
				.build();

		service.write(boardDTO);
	}
				
	File targetFile = null; 
	
	for(MultipartFile mpr:multipartFile) {
		
		String org_filename = mpr.getOriginalFilename();	
		String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));	
		String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;	
		long filesize = mpr.getSize();
		
		log.info("org_filename={}", org_filename);
		log.info("stored_filename={}", stored_filename);
		
		targetFile = new File(path + stored_filename);
		mpr.transferTo(targetFile);

		log.info("targerFile = {}", targetFile);

		BoardEntity boardEntity = service.view(seqno);

		/////////////////////////////////////////////////////////////////////////////////////////////
		log.info("boardentity!!!");
		/////////////////////////////////////////////////////////////////////////////////////////////
		
		MemberEntity memberEntity = memberService.memberInfo(userid);
		
		FileDTO fileInfo = new FileDTO();
		fileInfo.setOrg_filename(org_filename);
		fileInfo.setStored_filename(stored_filename);
		fileInfo.setFilesize(filesize);
		fileInfo.setSeqno(boardEntity);
		fileInfo.setUserid(memberEntity);
		fileInfo.setCheckfile("Y");

		log.info("org_filename = {}", fileInfo.getOrg_filename());
		log.info("stored_filename = {}", fileInfo.getStored_filename());
		log.info("filesize = {}", fileInfo.getFilesize());
		log.info("Seqno = {}", fileInfo.getSeqno());
		log.info("userid = {}", fileInfo.getUserid());
		
		service.fileInfoRegistry(fileInfo);

	}
}

//파일 다운로드
@GetMapping("/board/fileDownload")
public void fileDownload(@RequestParam(name="fileseqno" , required=false) Long fileseqno, HttpServletResponse rs) throws Exception {
	
	String path = "/Users/yy_ehh/Desktop/Springboot/ChooWarShop/boardfile/";
	
	FileEntity fileInfo = service.fileInfo(fileseqno);
	String org_filename = fileInfo.getOrg_filename();
	String stored_filename = fileInfo.getStored_filename();
	
	byte fileByte[] = FileUtils.readFileToByteArray(new File(path+stored_filename));
	
	rs.setContentType("application/octet-stream");
	rs.setContentLength(fileByte.length);
	rs.setHeader("Content-Disposition",  "attachment; fileName=\""+URLEncoder.encode(org_filename, "UTF-8")+"\";");
	rs.getOutputStream().write(fileByte);
	rs.getOutputStream().flush();
	rs.getOutputStream().close();
	
}
	
//게시물 수정 화면 보기
@GetMapping("/board/modify")
public void GetModify(Model model, 
		@RequestParam(name="deleteFileList", required=false) int[] deleteFileList,
		@RequestParam("page") int pageNum, @RequestParam("seqno") Long seqno,
		@RequestParam(name="searchType", required=false) String searchType,
		@RequestParam(name="keyword", required=false) String keyword) 
		throws Exception {
	
	BoardEntity board = service.view(seqno);
	
	model.addAttribute("view", service.view(seqno));
	model.addAttribute("seqno",seqno);
	model.addAttribute("page",pageNum);
	model.addAttribute("searchType",searchType);
	model.addAttribute("keyword",keyword);
	model.addAttribute("fileListView", service.fileListView(board));
}

//게시물 수정 하기
@PostMapping("/board/modify")
public String PostModify(BoardDTO boardDTO, @RequestParam("page") int pageNum,
		@RequestParam(name="deleteFileList", required=false) Long[] deleteFileList,
		@RequestParam(name="searchType", required=false) String searchType,
		@RequestParam(name="keyword", required=false) String keyword) throws Exception{
	
	String path = "/Users/yy_ehh/Desktop/Springboot/ChooWarShop/boardfile/";
	
	//게시물 업데이트
	service.modify(boardDTO);
	
	if(deleteFileList != null) {
		
		for(int i=0; i<deleteFileList.length; i++) {

			//파일 삭제
			FileEntity fileInfo = service.fileInfo(deleteFileList[i]);
			File file = new File(path + fileInfo.getStored_filename());
			file.delete();
			
			//파일 테이블에서 파일 정보 삭제
			service.deleteFileList(deleteFileList[i]);
			
		}
	}

	return "redirect:/board/view?seqno=" + boardDTO.getSeqno() + "&page=" + pageNum
			+ "&searchType=" + searchType + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
}

//게시물 삭제
@GetMapping("/board/delete")
public String GetDelete(@RequestParam("seqno") Long seqno) throws Exception{

	service.delete(seqno);		
	return "redirect:/board/list?page=1";
}

//댓글 처리

@ResponseBody
@PostMapping("/board/reply")
public List<ReplyInterface> postReply(ReplyInterface replyVO,@RequestParam("option") String option)throws Exception{
	
	switch(option) {
	
	case "I" : service.replyRegistry(replyVO); //댓글 등록
				break;
	case "U" : service.replyUpdate(replyVO); //댓글 수정
				break;
	case "D" : service.replyDelete(replyVO); //댓글 삭제
				break;
	}

	return service.replyView(replyVO);
}




}
