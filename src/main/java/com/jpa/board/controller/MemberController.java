package com.jpa.board.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jpa.board.dto.MemberDTO;
import com.jpa.board.entity.MemberEntity;
import com.jpa.board.service.MemberService;

@Log4j2
@RequiredArgsConstructor
@Controller
public class MemberController {

private final PasswordEncoder pwdEncoder;
private final MemberService service;

// 로그인 화면 보기
@RequestMapping(value="/member/login", method=RequestMethod.GET)
public void getLogin(Model model, @RequestParam(name = "message", required = false) String message) throws Exception{
	model.addAttribute("message", message);

}

// 로그인 화면 보기
@RequestMapping(value="/member/login",method=RequestMethod.POST)
public void postLogIn(Model model,@RequestParam(name="message", required=false) String message) throws Exception{ 

}

// 사용자 등록 화면 보기
@RequestMapping(value="/member/signup",method=RequestMethod.GET)
public void getMemberRegistry() throws Exception { }

// 사용자 등록 시 아이디 중복 확인
@ResponseBody
@RequestMapping(value="/member/idCheck", method=RequestMethod.POST)
public int idCheck(@RequestParam("userid") String userid) throws Exception{
	return service.idCheck(userid);

}

// 사용자 등록 화면 처리
@RequestMapping(value="/member/signup", method=RequestMethod.POST)
public String postMemberRegistry(MemberDTO memberDTO) throws Exception{

	String inputPassword = memberDTO.getUserpw();
	String pwd = pwdEncoder.encode(inputPassword); 
	memberDTO.setUserpw(pwd);
	memberDTO.setPwcheck(1);
	memberDTO.setRole("USER");


	service.memberInfoRegistry(memberDTO);
			
	return "redirect:/member/login";
}

// 유저 메인 페이지 보기
@RequestMapping(value="/user/userHomepage",method=RequestMethod.GET)
public void getUserPage(Model model,@RequestParam(name="message", required=false) String message) 
		throws Exception{ 
	
	model.addAttribute("message", message);
	
}

// 유저 메인 페이지 보기
@RequestMapping(value="/user/userHomepage",method=RequestMethod.POST)
public void postUserPage(Model model,@RequestParam(name="message", required=false) String message) 
		throws Exception{ 
	
	model.addAttribute("message", message);
	
}

// 유저 정보 보기 (마이페이지)
@RequestMapping(value="/user/mypage",method=RequestMethod.GET)
public void getUserMypage(Model model,HttpSession session) throws Exception {
	
	String userid = (String)session.getAttribute("userid");
	String username = (String)session.getAttribute("username");

	MemberEntity member = service.memberInfo(userid);

	model.addAttribute("userid", userid);
	model.addAttribute("username", username);
	model.addAttribute("telno", member.getTelno());
	model.addAttribute("address", member.getAddress());
	model.addAttribute("address2", member.getAddress2());
	
}

// 로그아웃
@RequestMapping(value="/user/logout",method=RequestMethod.POST)
public void postLogout(HttpSession session,Model model) throws Exception{}	

//사용자 패스워드 변경 보기
@RequestMapping(value="/user/memberPasswordModify", method=RequestMethod.GET)
public void getMemberPasswordModify() {}

//사용자 패스워드 변경 
@RequestMapping(value="/user/memberPasswordModify", method=RequestMethod.POST)
public String postMemberPasswordModify(@RequestParam("old_userpassword") String old_password,
		@RequestParam("new_userpassword") String new_password, HttpSession session) throws Exception{ 
	
	String userid = (String)session.getAttribute("userid");
	
	MemberEntity memberEntity = service.memberInfo(userid);
	MemberDTO memberDTO = new MemberDTO(memberEntity);

	if(pwdEncoder.matches(old_password, memberDTO.getUserpw())) {
		service.memberPasswordModify(userid, pwdEncoder.encode(new_password));
		
	}
	return "redirect:/member/login";

}

// 사용자 패스워드 임시 발급 보기
@RequestMapping(value="/member/searchPassword",method=RequestMethod.GET)
public void getSearchPassword() {} 

// 사용자 패스워드 임시 발급
@RequestMapping(value="/member/searchPassword",method=RequestMethod.POST)
public String postSearchPassword(MemberDTO member, RedirectAttributes rttr) throws Exception{ 
	
	if(service.searchPassword(member.getUserid(), member.getUsername()) == null) {
		
		rttr.addFlashAttribute("msg", "PASSWORD_NOT_FOUND");
		return "redirect:/member/searchPassword"; 
		
	}
	
	//숫자 + 영문대소문자 7자리 임시패스워드 생성
	StringBuffer tempPW = new StringBuffer();
	Random rnd = new Random();

	for (int i = 0; i < 7; i++) {
		int rIndex = rnd.nextInt(3);

		switch (rIndex) {
		case 0:
			// a-z : 아스키코드 97~122
			tempPW.append((char)((int)(rnd.nextInt(26)) + 97));
			break;

		case 1:
			// A-Z : 아스키코드 65~122
			tempPW.append((char)((int)(rnd.nextInt(26)) + 65));
			break;

		case 2:
			// 0-9
			tempPW.append((rnd.nextInt(10)));
			break;

		}
	}
	
	member.setUserpw(pwdEncoder.encode(tempPW));
	service.memberPasswordModify(member.getUserid(), member.getUserpw());

	log.info("임시 비밀번호 : " + tempPW);
	log.info("비밀번호 : " + member.getUserpw());

	return "redirect:/member/tempPWView?userpw=" + tempPW;

} 

// 발급한 임시패스워드 보기
@RequestMapping(value="/member/tempPWView",method=RequestMethod.GET)
public void getTempPWView(Model model, @RequestParam("userpw") String userpw) {
	
	model.addAttribute("userpw", userpw);
	
}

// 사용자 정보 수정 보기
@RequestMapping(value="/user/memberInfoModify", method=RequestMethod.GET)
public void getMemberInfoModify(Model model, HttpSession session) throws Exception{
	
	String userid = (String)session.getAttribute("userid");
	
	model.addAttribute("member", service.memberInfo(userid));
	
	log.info("사용자 정보 수정 보기");

}

// 사용자 정보 수정
@RequestMapping(value="/user/memberInfoModify", method=RequestMethod.POST)
public String postMemberInfoModify(MemberDTO memberDTO, HttpSession session) throws Exception{
	
	String userid = (String)session.getAttribute("userid");
	
	memberDTO.setUserid(userid);		
	service.memberInfoUpdate(memberDTO);
	
	return "redirect:/user/mypage";

}
	
// 사용자 아이디 찾기 보기
@RequestMapping(value="/member/searchID",method=RequestMethod.GET)
public void getSearchID() {} 


// 사용자 아이디 찾기 
@RequestMapping(value="/member/searchID",method=RequestMethod.POST)
public String postSearchID(MemberDTO member, RedirectAttributes rttr) throws Exception{ 
	MemberEntity memberEntity = service.searchID(member.getUsername(), member.getTelno());
			
	// 조건에 해당하는 사용자가 아닐 경우 
	if(memberEntity == null ) { 
		rttr.addFlashAttribute("msg", "ID_NOT_FOUND");
		return "redirect:/member/searchID"; 

	}
	return "redirect:/member/searchIDView?userid=" + memberEntity.getUserid();	

} 

// 찾은 아이디 보기
@RequestMapping(value="/member/searchIDView",method=RequestMethod.GET)
public void postSearchID(@RequestParam("userid") String userid, Model model) {
	
	model.addAttribute("userid", userid);
	
}

// 회원탈퇴
@RequestMapping(value = "/user/memberInfoDelete", method = RequestMethod.GET)
public String getMemberInfoDelete(HttpSession session) throws Exception {
	String userid = (String)session.getAttribute("userid");
	service.memberInfoDelete(userid);

	session.invalidate();

	return "redirect:/";

}



}
