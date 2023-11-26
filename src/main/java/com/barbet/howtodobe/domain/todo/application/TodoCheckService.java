package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoCheckRequestDTO;
import com.barbet.howtodobe.domain.todo.dto.TodoFixRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoCheckService {
    private final TodoRepository todoRepository;

    public boolean checkTodo(TodoCheckRequestDTO todoCheckRequestDTO){
        try {
            boolean newIsChecked = todoCheckRequestDTO.isChecked();
            newIsChecked = !newIsChecked;

            Todo _todo = todoRepository.findByTodoCategoryId(
                    todoCheckRequestDTO.getTodoId(),
                    todoCheckRequestDTO.getTodoCategoryId()).get();


            if (_todo == null) {
                throw new RuntimeException("해당하는 투두가 존재하지 않음.");
            }

            todoRepository.updateIsChecked(newIsChecked,
                    todoCheckRequestDTO.getTodoId(),
                    todoCheckRequestDTO.getTodoCategoryId());

            _todo = todoRepository.findById(todoCheckRequestDTO.getTodoId()).get();

            if (_todo.getTodoId() > 0){
                return _todo.isFixed();
            }
            else {
                throw new RuntimeException("해당하는 투두가 존재하지 않음.");
            }
        } catch (RuntimeException e){
            throw new RuntimeException("해당하는 투두가 존재하지 않음.");
        }
    }
}