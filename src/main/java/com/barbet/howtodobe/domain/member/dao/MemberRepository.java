package com.barbet.howtodobe.domain.member.dao;

import com.barbet.howtodobe.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // TODO 읽었으면 주석 삭제해줘!
    // NullPointerException를 방지용으로 Optional 처리해뒀어!
    // findByEmail도 Optional로 하면 좋을거 같습니당 (관련 로직 괜히 수정하다가 오류 날수도 있으니까 있단 나뒀어..!)
    // Optional로 묶고 exception 처리는 따로 global에 생성해둔 곳에서 custom 해서 쓰면 좋을거 같아
    Optional<Member> findByMemberId(Long memberId);
    Member findByEmail(String email);
    List<Member> findAll();
}
