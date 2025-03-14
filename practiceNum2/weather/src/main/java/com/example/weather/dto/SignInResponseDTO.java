package com.example.weather.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInResponseDTO {
    private boolean success;
    private String userId;
    private String userName;
}
