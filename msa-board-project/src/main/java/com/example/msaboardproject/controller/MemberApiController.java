package com.example.msaboardproject.controller;

import com.example.msaboardproject.config.jwt.TokenProvider;
import com.example.msaboardproject.config.security.CustomUserDetails;
import com.example.msaboardproject.dto.*;
import com.example.msaboardproject.model.Member;
import com.example.msaboardproject.service.MemberService;
import com.example.msaboardproject.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public SignInResponseDTO signIn(
            @RequestBody SignInRequestDTO signInRequestDTO,
            HttpServletResponse response
    ) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequestDTO.getUsername(),
                        signInRequestDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        Member member = ((CustomUserDetails) authenticate.getPrincipal()).getMember();

        String accessToken = tokenProvider.generateToken(member, Duration.ofHours(2));
        String refreshToken = tokenProvider.generateToken(member, Duration.ofDays(2));

        CookieUtil.addCookie(response,"refreshToken",refreshToken,7*24*60*60);

        return SignInResponseDTO.builder()
                .isLoggedIn(true)
                .userId(member.getUserId())
                .userName(member.getUserName())
                .token(accessToken)
                .build();
    }

    @PostMapping("/join")
    public SignUpResponseDTO signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        memberService.signUp(signUpRequestDTO.toMember(bCryptPasswordEncoder));
        return SignUpResponseDTO.builder()
                .successed(true)
                .build();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.deleteCookie(request, response,"refreshToken");
        CookieUtil.deleteCookie(request, response,"accessToken");
    }

    @GetMapping("/user/info")
    public UserInfoResponseDTO getUserInfo(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        Member member = (Member) request.getAttribute("member");
        CookieUtil.deleteCookie(request,response,"accessToken");
        return UserInfoResponseDTO.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .userName(member.getUserName())
                .role(member.getRole())
                .build();
    }
}
