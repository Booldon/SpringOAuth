package com.example.SessionRedis.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class RedisAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        System.out.println("Session1 : " + request.getSession().getAttribute("username"));
        System.out.println("Session2 : " + request.getSession().getId());

        String username = authentication.getName().toString();
        HttpSession session = request.getSession();

        session.setAttribute("username",username);
        session.setMaxInactiveInterval(3600);


    }
}
