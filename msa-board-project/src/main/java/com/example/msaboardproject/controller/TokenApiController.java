package com.example.msaboardproject.controller;

import com.example.msaboardproject.dto.RefreshTokenResponseDTO;
import com.example.msaboardproject.service.TokenApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TokenApiController {
    private final TokenApiService tokenApiService;

    @PostMapping("/refresh-token")
    public RefreshTokenResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return tokenApiService.refreshToken(request, response);
    }
}
