package com.example.weather.service;

import com.example.weather.config.security.CustomUserDetails;
import com.example.weather.mapper.MemberMapper;
import com.example.weather.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final MemberMapper memberMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberMapper.selectMemberByUserId(username);
        if (member == null) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return CustomUserDetails.builder()
                .member(member)
                .build();
    }
}