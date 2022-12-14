package com.jpa.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.board.entity.BoardEntity;
import com.jpa.board.entity.FileEntity;
import com.jpa.board.entity.MemberEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long>{

    public List<FileEntity> findBySeqno(BoardEntity board);
	public List<FileEntity> findByUserid(MemberEntity member);
}
