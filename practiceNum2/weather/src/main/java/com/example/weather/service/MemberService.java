package com.example.weather.service;

import com.example.weather.mapper.MemberMapper;
import com.example.weather.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;

    public void signUp(Member member) {
        memberMapper.insertMember(member);
    }

    public void signIn(Member member) {
    }
}