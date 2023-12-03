package com.barbet.howtodobe.domain.member.dao;

import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(Long memberId);
    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Member findByEmailForDuplicateCheck(@Param("email") String email);

    List<Member> findAll();
}
