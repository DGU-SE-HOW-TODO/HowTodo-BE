package com.barbet.howtodobe.domain.member.dao;

import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(Long memberId);
    Member findByEmail(String email);
    List<Member> findAll();
}
