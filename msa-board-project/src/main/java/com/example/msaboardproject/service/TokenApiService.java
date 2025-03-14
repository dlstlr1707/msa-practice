package com.example.msaboardproject.service;

import com.example.msaboardproject.config.jwt.TokenProvider;
import com.example.msaboardproject.dto.NaverUserInfoResponseDTO;
import com.example.msaboardproject.dto.RefreshTokenResponseDTO;
import com.example.msaboardproject.dto.SignInResponseDTO;
import com.example.msaboardproject.model.Member;
import com.example.msaboardproject.type.Role;
import com.example.msaboardproject.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenApiService {
    private final TokenProvider tokenProvider;
    private final OAuthService oAuthService;

    private String[] tokens;

    public RefreshTokenResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = CookieUtil.getCookieValue(request, "refreshToken");

        String type = checkTokenType(refreshToken);

        if(type != null && type.equals("naver")){
            SignInResponseDTO responseDTO = oAuthService.getReAccessToken("naver", tokens[1], response);

            String[] filterToken = responseDTO.getToken().split(":");

            NaverUserInfoResponseDTO userInfoResponseDTO = oAuthService.getNaverUserInfo(filterToken[1]);

            Member member = Member.builder()
                    .userId(userInfoResponseDTO.getName())
                    .userName(userInfoResponseDTO.getName())
                    .role(Role.ROLE_USER)
                    .build();

            response.setHeader(HttpHeaders.AUTHORIZATION, responseDTO.getToken());
            response.sendRedirect("/oauth/token");

            return RefreshTokenResponseDTO.builder()
                    .token(responseDTO.getToken())
                    .userId(member.getUserId())
                    .userName(member.getUserName())
                    .isValidate(true)
                    .build();
        }else if(type != null && type.equals("google")){
            return null;
        }else if(type != null && type.equals("kakao")){
            return null;
        }else{
            if(refreshToken != null && tokenProvider.validateToken(refreshToken) == 1) {
                Member member = tokenProvider.getTokenDetails(refreshToken);

                String newAccessToken = tokenProvider.generateToken(member, Duration.ofHours(2));
                String newRefreshToken = tokenProvider.generateToken(member, Duration.ofDays(2));

                CookieUtil.addCookie(response,"refreshToken",newRefreshToken,7*24*60*60);

                response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);

                return RefreshTokenResponseDTO.builder()
                        .token(newAccessToken)
                        .userId(member.getUserId())
                        .userName(member.getUserName())
                        .isValidate(true)
                        .build();
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
            }
        }
    }

    private String checkTokenType(String token) {
        tokens = null;
        if(token != null){
            tokens = token.split(":");

            switch(tokens[0]){
                case "naver":
                    return "naver";
                case "google":
                    return "google";
                case "kakao":
                    return "kakao";
                default:
                    return "our";
            }
        }else{
            return null;
        }
    }
}
