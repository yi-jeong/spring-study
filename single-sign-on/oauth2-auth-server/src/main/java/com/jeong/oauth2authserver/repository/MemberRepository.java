package com.jeong.oauth2authserver.repository;

import com.jeong.oauth2authserver.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUsername(String userId);
}
