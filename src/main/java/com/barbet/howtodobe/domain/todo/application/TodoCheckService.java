package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.member.dao.MemberRepository;
import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoCheckRequestDTO;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.CAN_NOT_TODO_CHECK;
import static com.barbet.howtodobe.global.exception.CustomErrorCode.TODO_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TodoCheckService {
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

//    public boolean checkTodo(TodoCheckRequestDTO todoCheckRequestDTO){
//        try {
//            boolean newIsChecked = todoCheckRequestDTO.getIsChecked();
//            newIsChecked = !newIsChecked;
//
//            Todo _todo = todoRepository.findByTodoCategoryId(
//                    todoCheckRequestDTO.getTodoId(),
//                    todoCheckRequestDTO.getTodoCategoryId()).get();
//
//
//            if (_todo == null) {
//                throw new RuntimeException("해당하는 투두가 존재하지 않음.");
//            }
//
//            todoRepository.updateIsChecked(newIsChecked,
//                    todoCheckRequestDTO.getTodoId(),
//                    todoCheckRequestDTO.getTodoCategoryId());
//
//            _todo = todoRepository.findById(todoCheckRequestDTO.getTodoId()).get();
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

    public void updateTodoChecked (Long todoId) {

        // TODO 임시 멤버
        Member tempMember = memberRepository.findByEmail("senuej37@gmail.com");

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));

        Boolean isChecked = todo.getIsChecked();
        if (todo.getFailtagName() == null) {
            // 이미 체크된 todo라면 해제하고, 해제된 todo라면 체크
            // 대신 실패태그 등록 안한 것만 가능
            if (isChecked == null) {
                isChecked = true;
            } else {
                isChecked = !isChecked; // 체크 상태를 반전시킴
            }
        } else {
            throw new CustomException(CAN_NOT_TODO_CHECK);
        }

        todo.updateTodoChecked(isChecked);
        todoRepository.save(todo);
    }
}