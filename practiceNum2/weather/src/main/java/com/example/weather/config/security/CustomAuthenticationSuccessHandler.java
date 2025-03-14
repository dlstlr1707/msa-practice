package com.example.weather.config.security;

import com.example.weather.dto.SignInResponseDTO;
import com.example.weather.model.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();

        // 세션
        HttpSession session = request.getSession();
        session.setAttribute("userId",member.getUserId());
        session.setAttribute("userName",member.getUserName());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json; charset=utf-8");

        System.out.println(member.getUserId());
        System.out.println(member.getUserName());

        SignInResponseDTO build = SignInResponseDTO.builder()
                .success(true)
                .userId(member.getUserId())
                .userName(member.getUserName())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(build));

        //response.sendRedirect("weather");
    }
}
