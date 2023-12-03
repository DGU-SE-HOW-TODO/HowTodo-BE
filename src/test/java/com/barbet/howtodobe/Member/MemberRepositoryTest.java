package com.barbet.howtodobe.Member;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void saveTest(){
        Member _member = Member.builder()
                .email("member0@gmail.com")
                .password("member0password")
                .nickname("memberzero")
                .build();
        Member member = memberRepository.save(_member);
        Assertions.assertEquals(member, _member);
    }

    @Test
    public void findByEmailTest(){
        Member _member = Member.builder()
                .email("member0@gmail.com")
                .password("member0password")
                .nickname("memberzero")
                .build();
        Member m = memberRepository.save(_member);

        Member member = memberRepository.findByEmailForDuplicateCheck(_member.getEmail());
        Assertions.assertEquals(member, _member);
        Assertions.assertEquals("member0@gmail.com", member.getEmail());
    }
}
