package com.jpa.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
        AuthenticationException exception) throws IOException, ServletException {
        // HttpServletRequest request -> request에 대한 정보
        // HttpServletResponse response -> response에 대해 설정 할 수 있는 변수
        // AuthenticationException exception -> 로그인 실패 시 예외에 대한 정보를 가지고 있다.

        String msg = "";
        
        // exception 관련 메세지 처리
        if (exception instanceof UsernameNotFoundException) { // 아이디가 일치하지 않는 경우
            msg = "EMAIL_NOT_FOUND";

        } else if (exception instanceof BadCredentialsException) { // 비밀번호가 일치하지 않는 경우
            msg = "AUTHORITY_NOT_PERMITTED";

        } else if (exception instanceof InternalAuthenticationServiceException) { // 시스템 문제로 내부 인증 관련 처리 요청을 할 수 없는 경우
            msg = "System_Error";

        } else {
            msg = "??_??";

        }

        setDefaultFailureUrl("/member/login?message=" + msg);
        super.onAuthenticationFailure(request, response, exception);    

    }


}
