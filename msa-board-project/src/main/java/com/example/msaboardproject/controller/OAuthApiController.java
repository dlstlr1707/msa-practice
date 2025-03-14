package com.example.msaboardproject.controller;

import com.example.msaboardproject.dto.SignInResponseDTO;
import com.example.msaboardproject.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthApiController {
    private final OAuthService oAuthService;

    @GetMapping("/naver/callback")
    public SignInResponseDTO naverCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws IOException {
        // 'code'는 네이버에서 받은 인증 코드입니다.
        SignInResponseDTO responseDTO = oAuthService.getAccessToken("naver",code, state, response);

        response.sendRedirect("/oauth/token");

        return responseDTO;
        // 사용자 정보를 사용하여 로그인 처리, 회원가입 등을 진행
        // 예를 들어, 로그인 처리를 하고 메인 페이지로 리디렉션
    }

}
