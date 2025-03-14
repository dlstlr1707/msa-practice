package com.example.msaboardproject.config.filter;

import com.example.msaboardproject.config.jwt.TokenProvider;
import com.example.msaboardproject.dto.NaverUserInfoResponseDTO;
import com.example.msaboardproject.model.Member;
import com.example.msaboardproject.service.OAuthService;
import com.example.msaboardproject.type.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    private final OAuthService oAuthService;

    private String[] tokens;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // accessToken 검증 로직

        String requestURI = request.getRequestURI();
        log.info("Request URI: " + requestURI);
        // refreshToken 검증이 넘어온다면 다음으로 넘겨버림
        // 추후 filterChain config로 설정 바꿀수있음
        if("/refresh-token".equals(requestURI)){
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        String type = checkTokenType(token);

        if(type != null && type.equals("naver")){
            // 네이버 회원 정보 요청
            NaverUserInfoResponseDTO responseDTO = oAuthService.getNaverUserInfo(tokens[1]);
            if( responseDTO == null ){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Authentication authentication = oAuthService.getAuthentication(tokens[1],responseDTO.getName());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = Member.builder()
                    .userId(responseDTO.getName())
                    .userName(responseDTO.getName())
                    .role(Role.ROLE_USER)
                    .build();
            request.setAttribute("member", member);

        }else if(type != null && type.equals("google")){

        }else if(type != null && type.equals("kakao")){

        }else{
            if (token != null && tokenProvider.validateToken(token)==1) {
                // 토큰이 유효할 경우, 인증 정보를 설정
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                Member member = tokenProvider.getTokenDetails(token);
                request.setAttribute("member", member); //SecurityContextHolder에 정보가 있지만 이방법도 있다.

            }else if(token != null && tokenProvider.validateToken(token)==2){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            // bearer 를 제외한 순수 jwt토큰만 반환함
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    private String checkTokenType(String token) {
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
