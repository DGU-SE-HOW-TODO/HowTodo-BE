package com.barbet.howtodobe.domain.member.dao;

import com.barbet.howtodobe.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(Long memberId);
    Member findByEmail(String email);
    List<Member> findAll();
}
