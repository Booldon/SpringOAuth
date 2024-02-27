package com.example.SessionRedis.config;


import com.example.SessionRedis.service.RedisAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();

        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER\n");

        return hierarchy;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers( "/", "/login", "/loginProc", "/join", "joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("USER")
                        .anyRequest().authenticated());

        http
                .formLogin((auth) -> auth.loginPage("/login") //자동 로그인 페이지 리다이렉션
                        .loginProcessingUrl("/loginProc") //로그인 폼 받을 URL 명시
                        .permitAll()
                        .successHandler(new RedisAuthenticationSuccessHandler()));


//        http
//                .logout((auth) -> auth
//                        .logoutSuccessUrl("/"));

        http
                .csrf((auth) -> auth.disable());

        http    // 동시 로그인 설정
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) //한 개의 아이디에 대한 maxSession
                        .maxSessionsPreventsLogin(true)); // maxSession 초과 시 따를 정책 
                                                          // true : 초과시 새로운 로그인 차단
                                                          // false : 초과시 기존 세션 하나 삭제

        http    // session 보안 정책
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());

        return http.build();
    }
}
