package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoFixRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoFixService {
    private final TodoRepository todoRepository;

    public boolean fixTodo(TodoFixRequestDTO todoFixRequestDTO){
        try {
            boolean newIsFixed = todoFixRequestDTO.isFixed();
            System.out.println("IsFixed:" + newIsFixed);
            newIsFixed = !newIsFixed;
            System.out.println("newIsFixed:" + newIsFixed);

            Todo _todo = todoRepository.findByTodoCategoryId(
                    todoFixRequestDTO.getTodoId(),
                    todoFixRequestDTO.getTodoCategoryId()).get();


            if (_todo == null) {
                throw new RuntimeException("해당하는 투두가 존재하지 않음.");
            }

            todoRepository.updateIsFixed(newIsFixed,
                    todoFixRequestDTO.getTodoId(),
                    todoFixRequestDTO.getTodoCategoryId());

            _todo = todoRepository.findById(todoFixRequestDTO.getTodoId()).get();

            System.out.println("updatedFixed:" + _todo.isFixed());

            if (_todo.getTodoId() > 0){
                return newIsFixed;
            }
            else {
                throw new RuntimeException("해당하는 투두가 존재하지 않음.");
            }
        } catch (RuntimeException e){
            throw new RuntimeException("해당하는 투두가 존재하지 않음.");
        }
    }
}
