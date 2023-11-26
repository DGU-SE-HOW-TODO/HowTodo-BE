package com.barbet.howtodobe.domain.todo.application;

import com.barbet.howtodobe.domain.calendar.dao.CalendarRepository;
import com.barbet.howtodobe.domain.calendar.domain.Calendar;
import com.barbet.howtodobe.domain.category.dao.CategoryRepository;
import com.barbet.howtodobe.domain.category.domain.Category;
import com.barbet.howtodobe.domain.todo.dao.TodoRepository;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoAssignRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class TodoAssignService {
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    private final CalendarRepository calendarRepository;

    public Todo assignTodo(TodoAssignRequestDTO todoAssignRequestDTO) {

        try {
            Date date = Date.valueOf(todoAssignRequestDTO.getSelectedDate());

            Calendar cal = calendarRepository.findByDate(date).get();

            if (cal == null)
                throw new RuntimeException("날짜에 맞는 캘린더가 존재하지 않습니다.");
            Category category = categoryRepository.findById(todoAssignRequestDTO.getTodoCategoryId()).get();
            if (category == null)
                throw new RuntimeException("카테고리가 존재하지 않습니다.");

            Todo todo = Todo.builder()
                    .calendar(cal)
                    .category(category)
                    .name(todoAssignRequestDTO.getTodo())
                    .priority(todoAssignRequestDTO.getPriority())
                    .build();

            Todo _todo = todoRepository.save(todo);
            System.out.println("todo:"+_todo);
            if (_todo.getTodoId() > 0) {
                return _todo;
            }
            else {
                throw new RuntimeException("투두 등록 실패");
            }
        }catch (RuntimeException e){
            throw new RuntimeException("투두 등록 실패");
        }
    }
}