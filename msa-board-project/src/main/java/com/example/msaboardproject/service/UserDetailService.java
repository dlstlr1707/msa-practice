package com.example.msaboardproject.service;

import com.example.msaboardproject.config.security.CustomUserDetails;
import com.example.msaboardproject.mapper.MemberMapper;
import com.example.msaboardproject.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final MemberMapper memberMapper;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberMapper.findByUserId(username);
        if(member == null) {
            throw new UsernameNotFoundException(username + "not found");
        }

        return CustomUserDetails.builder()
                .member(member)
                .roles(List.of(String.valueOf(member.getRole().name())))
                .build();
    }
}
