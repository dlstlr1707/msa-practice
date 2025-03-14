package com.example.weather.dto;

import com.example.weather.model.Member;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@ToString
public class SignInRequestDTO {
    private String userId;
    private String password;

    public Member toMember(BCryptPasswordEncoder passwordEncoder) {
        return Member.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .build();
    }
}