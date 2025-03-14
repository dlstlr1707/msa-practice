package com.example.msaboardproject.service;

import com.example.msaboardproject.client.NaverProfileClient;
import com.example.msaboardproject.client.NaverTokenClient;
import com.example.msaboardproject.config.jwt.TokenProvider;
import com.example.msaboardproject.dto.NaverTokenResponseDTO;
import com.example.msaboardproject.dto.NaverUserInfoResponseDTO;
import com.example.msaboardproject.dto.SignInResponseDTO;
import com.example.msaboardproject.model.Member;
import com.example.msaboardproject.model.NaverUserInfo;
import com.example.msaboardproject.type.Role;
import com.example.msaboardproject.util.CookieUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OAuthService {
    private final NaverTokenClient naverTokenClient;
    private final NaverProfileClient naverProfileClient;

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value("${oauth.naver.client-secret}")
    private String clientSecret;

    @Value("${oauth.naver.redirect-uri}")
    private String redirectUri;

    public SignInResponseDTO getAccessToken(String type, String code, String state, HttpServletResponse response) {

        NaverTokenResponseDTO responseDTO = naverTokenClient.getAccessToken("authorization_code", clientId, clientSecret, code, state);

        CookieUtil.addCookie(response, "refreshToken",type + ":" + responseDTO.getRefresh_token(), 7*24*60*60 );
        CookieUtil.addCookie(response, "accessToken",type + ":" + responseDTO.getAccess_token(), 60*60 );

        return SignInResponseDTO.builder()
                .isLoggedIn(true)
                .token(responseDTO.getAccess_token())
                .build();
    }

    public SignInResponseDTO getReAccessToken(String type, String refreshToken, HttpServletResponse response) {

        NaverTokenResponseDTO responseDTO = naverTokenClient.getReAccessToken("refresh_token", clientId, clientSecret, refreshToken );

        CookieUtil.addCookie(response, "refreshToken",type + ":" + responseDTO.getRefresh_token(), 7*24*60*60 );
        CookieUtil.addCookie(response, "accessToken",type + ":" + responseDTO.getAccess_token(), 60*60 );

        return SignInResponseDTO.builder()
                .isLoggedIn(true)
                .token(type + ":" + responseDTO.getAccess_token())
                .build();
    }

    public NaverUserInfoResponseDTO getNaverUserInfo(String token) {
        try {
            System.out.println("getNaverUserInfo in token :: "+token);
            String result = naverProfileClient.getNaverProfile("Bearer " + token);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);

            return NaverUserInfoResponseDTO.builder()
                    .id(jsonNode.get("response").get("id").asText())
                    .name(jsonNode.get("response").get("name").asText())
                    .nickname(jsonNode.get("response").get("nickname").asText())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Authentication getAuthentication(String token, String name) {
        // Claims에서 역할을 추출하고, GrantedAuthority로 변환
        List<GrantedAuthority> authorities = Collections.singletonList(
                // 권한은 리스트로 여러 개 넣어 줄 수도 있다.
                new SimpleGrantedAuthority(Role.ROLE_USER.name())
        );

        // UserDetails 객체 생성
        UserDetails userDetails = new User(name,"",authorities);

        // spring security에 인증객체 생성한거 등록 해줌 (컨버팅)
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

}
