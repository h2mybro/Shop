package com.jpa.board;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final AuthSucessHandler authSucessHandler;
    private final AuthFailureHandler authFailureHandler;    

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests() 	    	
            .antMatchers("/", "/member/**", "/css/**", "/images/**", "/item/**"
                ,"/fragments/**").permitAll()
            .antMatchers("/user/**", "/board/**").hasAnyAuthority("USER","ADMIN")
            .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
            .anyRequest().authenticated();

	    //CSRF, CORS 공격 방어 비활성화
        http.csrf().disable();
        http.cors().disable();

        //로그인 처리
        http.formLogin()
            .usernameParameter("userid")
            .passwordParameter("userpw")
            .loginPage("/member/login") // 로그인 페이지 링크
            .successHandler(authSucessHandler) // 성공시 요청을 처리할 핸들러
            .failureHandler(authFailureHandler); // 실패시 요청을 처리할 핸들러

        //로그아웃 처리
        http.logout()
            .logoutUrl("/user/logout")
            //.logoutRequestMatcher(new AntPathRequestMatcher("/userManage/logout")) // 로그아웃 URL
            .logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소
            .invalidateHttpSession(true) // 세션 삭제
            .deleteCookies("JSESSIONID","remember-me") // JSESSIONID, remember-me 쿠키 삭제
            .permitAll();
        
        //세션 처리
        http.sessionManagement()
            .maximumSessions(1) // 세션 최대 허용 수 1, -1인 경우 무제한 세션 허용
            .maxSessionsPreventsLogin(false) // true면 중복 로그인을 막고, false면 이전 로그인의 세션을 해제
            .expiredUrl("/member/login?exception=SESSION_OUT"); // 세션이 만료된 경우 이동 할 페이지를 지정
    

        log.info("Application 접근 권한 설정 완료 !!!");
        return http.build();
            
    }

}
