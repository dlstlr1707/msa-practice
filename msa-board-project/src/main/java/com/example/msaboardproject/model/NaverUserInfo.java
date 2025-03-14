package com.example.msaboardproject.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NaverUserInfo {
    private String id;
    private String nickname;
    private String name;
    private String email;
    private String gender;
    private String age;
    private String birthday;
    private String profile_image;
    private String birthyear;
    private String mobile;
}
