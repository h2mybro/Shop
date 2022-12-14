package com.jpa.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.board.dto.MemberDTO;
import com.jpa.board.entity.MemberEntity;
import com.jpa.board.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@RequiredArgsConstructor
@Log4j2
@Service
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;

    // 사용자 등록
    @Override
    @Transactional
    public void memberInfoRegistry(MemberDTO memberDTO) throws Exception {
        
        MemberEntity memberEntity= memberDTO.dtoToEntity(memberDTO);
		memberRepository.save(memberEntity);

    }

    //로그인 시 중복 아이디 찾기
	@Override
	public int idCheck(String userid) throws Exception{
		
		return memberRepository.findById(userid).isEmpty()==true?0:1;
        
	}

    // 사용자 정보 보기
    @Override
    public MemberEntity memberInfo(String userid) throws Exception {

        return memberRepository.findById(userid).get();
        
    }

    // 사용자 패스워드 신규 발급을 위한 확인
	@Override
	public MemberEntity searchPassword(String userid, String username) throws Exception{
		return memberRepository.findByUseridAndUsername(userid, username);

	}

    // 사용자 패스워드 변경
	@Override
	public void memberPasswordModify(String userid, String userpw) throws Exception{
		MemberEntity member = memberRepository.findById(userid).get();
		member.memberPasswordModify(userpw);
		memberRepository.save(member);

	}

    // 사용자 정보 수정
	public void memberInfoUpdate(MemberDTO memberDTO) throws Exception{
				
		MemberEntity memberEntity = memberRepository.findById(memberDTO.getUserid()).get();
		
        String mdto_address2 = memberDTO.getAddress2();
        mdto_address2 = mdto_address2.replaceAll(",", "");

		memberEntity.memberInfoUpdate(memberDTO.getZipcode(), memberDTO.getAddress(), mdto_address2, memberDTO.getTelno());      

		memberRepository.save(memberEntity);

        log.info("사용자 정보 수정 save");
		
	}

    // 사용자 아이디 찾기
    @Override
    public MemberEntity searchID(String username, String telno) throws Exception {
        return memberRepository.findByUsernameAndTelno(username, telno);
        
    }

    // 회원 탈퇴
    @Override
    public void memberInfoDelete(String userid) {
        MemberEntity memberEntity = memberRepository.findById(userid).get();
        memberRepository.delete(memberEntity);

    }

}
