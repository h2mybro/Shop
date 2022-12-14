package com.jpa.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jpa.board.dto.BoardDTO;
import com.jpa.board.dto.FileDTO;
import com.jpa.board.dto.ItemDTO;
import com.jpa.board.dto.ReplyInterface;
import com.jpa.board.entity.BoardEntity;
import com.jpa.board.entity.FileEntity;
import com.jpa.board.entity.MemberEntity;
import com.jpa.board.service.BoardService;
import com.jpa.board.service.ItemService;
import com.jpa.board.service.MemberService;
import com.jpa.board.util.PageUtil;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@AllArgsConstructor
@Controller
public class AdminController {
private final BoardService service; //의존성 주입
private final MemberService memberService;
private final ItemService itemService;

// 상품 페이지 보기
@GetMapping(value="/admin/item/itemPage")
public String getItemPage(Model model) {
	model.addAttribute("itemDTO", new ItemDTO());

	return "/admin/item/itemPage";
	
}

// 상품 페이지 보기
@PostMapping(value="/admin/item/itemPage")
public String postItemPage(@Valid ItemDTO itemDTO, BindingResult bindingResult, Model model, 
	@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
	
	// 상품 등록 시 필수 값이 없다면 다시 상품 등록 페이지로 전환한다.
	if(bindingResult.hasErrors()) { 
		return "/admin/item/itemPage";
	}
	
	// 상품 등록 시 첫 번째 이미지가 없다면 에러 메세지와 함께 상품 등록 페이지로 전환합니다.
	// 상품의 첫 번째 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해서 필수 값으로 지정한다.
	if(itemImgFileList.get(0).isEmpty() && itemDTO.getId() == null) {
		model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
		return "/admin/item/itemPage";

	}

	try {
		// 상품 저장 로직을 호출. 매개 변수로 상품 정보와 상품 이미지 정보를 담고 있는 itemImgFileList를 넘겨준다.
		itemService.saveItem(itemDTO, itemImgFileList);

	}catch (Exception e) {
		model.addAttribute("errorMessage", "상품 등록 중 에러 발생" + e);
		return "/admin/item/itemPage";

	}
	

	// 상품이 정상적으로 등록되었으면 메인 페이지로 이동
	return "redirect:/";

}

// 관리자 로그인 화면 보기
@RequestMapping(value="/admin/adminHomepage", method=RequestMethod.GET)
public void getAdminLogin(Model model, @RequestParam(name = "message", required = false) String message) throws Exception{
	model.addAttribute("message", message);

}

// 관리자 로그인 화면 보기
@RequestMapping(value="/admin/adminHomepage",method=RequestMethod.POST)
public void postAdminLogIn(Model model,@RequestParam(name="message", required=false) String message) throws Exception{ 
	model.addAttribute("message", message);

}

// 관리자 화면 보기
@RequestMapping(value="/admin/adminManage", method=RequestMethod.GET)
public void getAdminManage(Model model, @RequestParam(name = "message", required = false) String message) throws Exception{
	model.addAttribute("message", message);
}

// 관리자 화면 보기
@RequestMapping(value="/admin/adminManage",method=RequestMethod.POST)
public void postAdminManage(Model model,@RequestParam(name="message", required=false) String message) throws Exception{ 
	model.addAttribute("message", message);
}

// board 폴더를 보기 위함
@RequestMapping(value="/admin/board/**", method=RequestMethod.GET)
public void getAdminboard(Model model, @RequestParam(name = "message", required = false) String message) throws Exception{
	model.addAttribute("message", message);
}

// board 폴더를 보기 위해서어ㅓ어어
@RequestMapping(value="/admin/board/**",method=RequestMethod.POST)
public void postAdminboard(Model model,@RequestParam(name="message", required=false) String message) throws Exception{ 
	model.addAttribute("message", message);
}

//실험중
@RequestMapping(value="/admin/item/**", method=RequestMethod.GET)
public void getAdminitem(Model model, @RequestParam(name = "message", required = false) String message) throws Exception{
	model.addAttribute("message", message);
}

// 로그인 화면 보기
@RequestMapping(value="/admin/item/**",method=RequestMethod.POST)
public void postAdminitem(Model model,@RequestParam(name="message", required=false) String message) throws Exception{ 
	model.addAttribute("message", message);
}

// 관리자 로그인 화면 보기
@RequestMapping(value="/admin/user/**", method=RequestMethod.GET)
public void getAdminUsermanage(Model model, @RequestParam(name = "message", required = false) String message) throws Exception{
	model.addAttribute("message", message);

}

// 관리자 로그인 화면 보기
@RequestMapping(value="/admin/user/**",method=RequestMethod.POST)
public void postAdminUsermanage(Model model,@RequestParam(name="message", required=false) String message) throws Exception{ 
	model.addAttribute("message", message);

}


//게시물 목록 보기
@GetMapping("/admin/board/list")
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
@GetMapping("/admin/board/view")
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
@GetMapping("/admin/board/write")
public void GetWrite() { }


//첨부 파일 있는 게시물 등록
@PostMapping("/admin/board/writeWithFile")
public String PostWriteWithFile(BoardDTO board, HttpServletRequest request) throws Exception{

	log.info("<-------------- 첨부 파일 있음 ------------------->");

	Long seqno = service.getSeqnoWithLastNumber();
	log.info("seqno={}"+seqno);
	// board.setSeqno(seqno);

	// service.modify(board);

	
	return "redirect:/admin/board/list?page=1";
}

//첨부 파일 없는 게시물 등록
@PostMapping("/admin/board/write")
public String PostWrite(BoardDTO board) throws Exception{
	
	log.info("<-------------- 첨부 파일 없음 ------------------->");
	Long seqno = service.getSeqnoWithNextval();
	board.setSeqno(seqno);
	
	service.write(board);
	
	return "redirect:/admin/board/list?page=1";
}

//파일 업로드
@ResponseBody
@PostMapping("/admin/board/fileUpload")
public void postFileUpload(@RequestParam("SendToFileList") List<MultipartFile> multipartFile, 
		@RequestParam("kind") String kind,@RequestParam(name="seqno", defaultValue="0", required=false) Long seqno,
		HttpSession session) throws Exception{
	
	log.info("파일 전송...");
	String path = "/Users/gpwls/Desktop/SpringBoot/file/"; 
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

	
//게시물 수정 화면 보기
@GetMapping("/admin/board/modify")
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
@PostMapping("/admin/board/modify")
public String PostModify(BoardDTO boardDTO, @RequestParam("page") int pageNum,
		@RequestParam(name="deleteFileList", required=false) Long[] deleteFileList,
		@RequestParam(name="searchType", required=false) String searchType,
		@RequestParam(name="keyword", required=false) String keyword) throws Exception{
	
	String path = "/Users/gpwls/Desktop/SpringBoot/file/";
	
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

	return "redirect:/admin/board/view?seqno=" + boardDTO.getSeqno() + "&page=" + pageNum
			+ "&searchType=" + searchType + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
}

//게시물 삭제
@GetMapping("/admin/board/delete")
public String GetDelete(@RequestParam("seqno") Long seqno) throws Exception{

	service.delete(seqno);		
	return "redirect:/admin/board/list?page=1";
}

//댓글 처리

@ResponseBody
@PostMapping("/admin/board/reply")
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
