package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoRequestDTO;
import com.barbet.howtodobe.global.exception.CustomErrorCode;
import com.barbet.howtodobe.global.exception.CustomException;
import com.barbet.howtodobe.global.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    /** To do 체크 */
    public void todoCheck(TodoRequestDTO request) {
        Member member = memberRepository.findByMemberId(tokenProvider.getMemberId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));


        // TODO toEntity 데이터 처리 로직 추가하기

        //



        Todo todo = request.toEntity(member);
        todoRepository.save(todo);
    }
}
