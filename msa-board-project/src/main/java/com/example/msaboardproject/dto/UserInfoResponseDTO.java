package com.example.msaboardproject.dto;

import com.example.msaboardproject.type.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponseDTO {
    private Long id;
    private String userName;
    private String userId;
    private Role role;
}
