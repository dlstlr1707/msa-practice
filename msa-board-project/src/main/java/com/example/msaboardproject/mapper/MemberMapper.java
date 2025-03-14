package com.example.msaboardproject.mapper;

import com.example.msaboardproject.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void saveMember(Member member);
    Member findByUserId(String userId);
}
