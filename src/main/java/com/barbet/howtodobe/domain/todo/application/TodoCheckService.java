package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.global.util.JwtTokenProvider;
import com.barbet.howtodobe.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.barbet.howtodobe.global.common.exception.CustomResponseCode.*;

@Service
@RequiredArgsConstructor
public class TodoCheckService {
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void updateTodoChecked (Long todoId) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));

        Boolean isChecked = todo.getIsChecked();
        if (todo.getFailtagName() == null) {
            if (isChecked == null) {
                isChecked = true;
            } else {
                isChecked = !isChecked;
            }
        } else {
            throw new CustomException(CAN_NOT_TODO_CHECK);
        }

        todo.updateTodoChecked(isChecked, member);
        todoRepository.save(todo);
    }
}