package com.jeong.oauth2authserver.service;

import com.jeong.oauth2authserver.entity.Member;
import com.jeong.oauth2authserver.entity.MemberDto;
import com.jeong.oauth2authserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) {
        Member member = memberRepository.findByUsername(id).orElseThrow();

        return MemberDto
                .builder()
                .userName(member.getUsername())
                .password(member.getPassword())
                .build();
    }

}