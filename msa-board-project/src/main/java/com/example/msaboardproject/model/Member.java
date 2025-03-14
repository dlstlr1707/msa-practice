package com.example.msaboardproject.model;

import com.example.msaboardproject.type.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Member {
    private Long id;
    private String userId;
    private String password;
    private String userName;
    private Role role;
}
