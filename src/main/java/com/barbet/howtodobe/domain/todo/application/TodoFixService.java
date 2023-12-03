package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoFixRequestDTO;
import com.barbet.howtodobe.global.eunse.JwtTokenProvider;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class TodoFixService {
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;


//    public boolean fixTodo(TodoFixRequestDTO todoFixRequestDTO){
//        try {
//            boolean newIsFixed = todoFixRequestDTO.getIsFixed();
//            newIsFixed = !newIsFixed;
//
//            Todo _todo = todoRepository.findByTodoCategoryId(
//                    todoFixRequestDTO.getTodoId(),
//                    todoFixRequestDTO.getTodoCategoryId()).get();
//
//
//            if (_todo == null) {
//                throw new RuntimeException("해당하는 투두가 존재하지 않음.");
//            }
//
//            todoRepository.updateIsFixed(newIsFixed,
//                    todoFixRequestDTO.getTodoId(),
//                    todoFixRequestDTO.getTodoCategoryId());
//
//            _todo = todoRepository.findById(todoFixRequestDTO.getTodoId()).get();
//
//            if (_todo.getTodoId() > 0){
//                return _todo.getIsFixed();
//            }
//            else {
//                throw new RuntimeException("해당하는 투두가 존재하지 않음.");
//            }
//        } catch (RuntimeException e){
//            throw new RuntimeException("해당하는 투두가 존재하지 않음.");
//        }
//    }

    public void updateTodoFixed (Long todoId) {
        Member member = memberRepository.findByMemberId(jwtTokenProvider.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));

        if (!todo.getIsFixed()) {
            todo.updateTodoFixed(true, member);
        }
        todoRepository.save(todo);
    }
}
