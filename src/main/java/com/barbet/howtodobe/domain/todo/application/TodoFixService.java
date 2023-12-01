package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoFixRequestDTO;
import com.barbet.howtodobe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.barbet.howtodobe.global.exception.CustomErrorCode.CAN_NOT_TODO_CHECK;
import static com.barbet.howtodobe.global.exception.CustomErrorCode.TODO_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TodoFixService {
    private final TodoRepository todoRepository;

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

        // TODO 임시 멤버
        // Member tempMember = memberRepository.findByEmail("senuej37@gmail.com");

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));

        if (todo.getIsFixed() == null) {
            todo.updateTodoFixed(true);
        }
        todoRepository.save(todo);
    }
}
