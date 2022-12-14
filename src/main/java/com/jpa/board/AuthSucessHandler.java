package com.jpa.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.jpa.board.entity.MemberEntity;
import com.jpa.board.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Component
public class AuthSucessHandler extends SimpleUrlAuthenticationSuccessHandler{
    
    private final MemberService service;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //사용 정보 중 아이디(이메일),이름, 권한 정보 가져 오기
        try {
            MemberEntity memberEntity = service.memberInfo(authentication.getName());

            //세션 등록하기
            HttpSession session = request.getSession();              
            session.setAttribute("userid",memberEntity.getUserid());
            session.setAttribute("username",memberEntity.getUsername());
            session.setAttribute("role", memberEntity.getRole());
            log.info("Session 설정 완료 !!!");

            log.info("role = " + memberEntity.getRole());

            if(memberEntity.getRole().equals("ADMIN")) {
                setDefaultTargetUrl("/admin/adminHomepage");
    
            }else if (memberEntity.getRole().equals("USER")) {
                setDefaultTargetUrl("/user/userHomepage");
    
            }else {
                setDefaultTargetUrl("/member/login");
                log.info("버그사용자 발견");
                
            }

        }catch(Exception e) { 
            e.printStackTrace(); 

        }

        super.onAuthenticationSuccess(request, response, authentication);


    }

}
