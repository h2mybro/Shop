package com.jpa.board.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.board.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    Optional<MemberEntity> findByUserid(String userid);

    //패스워드 임시 발급을 위한 아이디 확인
	public MemberEntity findByUseridAndUsername(String userid, String username);

    // 사용자 아이디 찾기
    public MemberEntity findByUsernameAndTelno(String username, String telno);
}
