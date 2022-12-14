package com.jpa.board.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jpa.board.entity.MemberEntity;
import com.jpa.board.entity.repository.MemberRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MemberUserDetailsService implements UserDetailsService {
    
    @Autowired
    MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
    
        log.info("MemberUserDetailsService loadUserByUsername = " + userid);

        Optional<MemberEntity> memberAuth = memberRepository.findById(userid);
        if(!memberAuth.isPresent()) {
            throw new UsernameNotFoundException("아이디/패스워드가 부정확합니다.");
        }

        MemberEntity member = memberAuth.get();

        List grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole());
        grantedAuthorities.add(grantedAuthority);

        User user = new User(userid, member.getUserpw(), grantedAuthorities);

        log.info("role = " + member.getRole());

        return user;
    }
    
}
