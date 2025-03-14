package com.example.msaboardproject.service;

import com.example.msaboardproject.mapper.MemberMapper;
import com.example.msaboardproject.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;

    public void signUp(Member member) {
        memberMapper.saveMember(member);
    }
}
