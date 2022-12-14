package com.jpa.board.service;

import com.jpa.board.dto.MemberDTO;
import com.jpa.board.entity.MemberEntity;

public interface MemberService {
    
    // 사용자 등록 서비스
    public void memberInfoRegistry(MemberDTO memberDTO) throws Exception;
    
    // 로그인 시 중복 아이디 찾기
	public int idCheck(String email) throws Exception;

    //사용자 정보 보기
	public MemberEntity memberInfo(String userid) throws Exception;

    //사용자 정보 수정
	public void memberInfoUpdate(MemberDTO memberDTO) throws Exception;

    // 사용자 패스워드 변경
	public void memberPasswordModify(String userid, String userpw) throws Exception;

    //사용자 패스워드 임시 발급을 위한 확인
	public MemberEntity searchPassword(String userid,String username) throws Exception;

    // 사용자 아이디 찾기
    public MemberEntity searchID(String username, String telno) throws Exception;

    // 회원 탈퇴
    public void memberInfoDelete(String userid);

}
