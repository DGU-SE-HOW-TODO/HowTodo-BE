package com.barbet.howtodobe.domain.todo.api;

import com.barbet.howtodobe.domain.calendar.application.UpdateSuccessRateService;
import com.barbet.howtodobe.domain.todo.application.TodoAssignService;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.TodoAssignRequestDTO;
import com.barbet.howtodobe.global.common.response.ApiStatus;
import com.barbet.howtodobe.global.common.response.HowTodoStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoApi {
    private final TodoAssignService todoAssignService;
    private final UpdateSuccessRateService updateSuccessRateService;

    @PostMapping(value = "/todo/assign", produces = "application/json")
    public ResponseEntity<ApiStatus> assign(@RequestBody TodoAssignRequestDTO todoAssignRequestDTO){
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Todo newTodo = todoAssignService.assignTodo(todoAssignRequestDTO);
            if (newTodo.getTodoId() > 0){
                updateSuccessRateService.updateSuccessRate(newTodo.getCalendar().getCalendarId());
                return new ResponseEntity(new ApiStatus(HowTodoStatus.OK, "투두 등록 완료"),
                        httpHeaders, HttpStatus.OK);
            }
            else {
                return new ResponseEntity(
                        new ApiStatus(HowTodoStatus.INTERNEL_SERVER_ERROR, "투두 등록 실패"),
                        httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (RuntimeException e){
            return new ResponseEntity(
                    new ApiStatus(HowTodoStatus.INTERNEL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
