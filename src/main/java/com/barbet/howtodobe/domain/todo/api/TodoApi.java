package com.barbet.howtodobe.domain.todo.api;

import com.barbet.howtodobe.domain.calendar.application.UpdateSuccessRateService;
import com.barbet.howtodobe.domain.todo.application.TodoAssignService;
import com.barbet.howtodobe.domain.todo.application.TodoCheckService;
import com.barbet.howtodobe.domain.todo.application.TodoFixService;
import com.barbet.howtodobe.domain.todo.application.TodoWithFailtagService;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.*;
import com.barbet.howtodobe.global.common.exception.CustomResponseCode;
import com.barbet.howtodobe.global.common.response.ApiStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoApi {
    private final TodoAssignService todoAssignService;
    private final UpdateSuccessRateService updateSuccessRateService;
    private final TodoFixService todoFixService;
    private final TodoCheckService todoCheckService;
    private final TodoWithFailtagService todoWithFailtagService;

    @PostMapping(value = "/assign", produces = "application/json")
    public ResponseEntity<ApiStatus> assign(@RequestBody TodoAssignRequestDTO todoAssignRequestDTO){
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Todo newTodo = todoAssignService.assignTodo(todoAssignRequestDTO);
            if (newTodo.getTodoId() > 0){
                updateSuccessRateService.updateSuccessRate(newTodo.getCalendar().getCalendarId());
                return new ResponseEntity(new ApiStatus(CustomResponseCode.SUCCESS, "투두 등록 완료"),
                        httpHeaders, HttpStatus.OK);
            }
            else {
                return new ResponseEntity(
                        new ApiStatus(CustomResponseCode.INTERNAL_SERVER_ERROR, "투두 등록 실패"),
                        httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (RuntimeException e){
            return new ResponseEntity(
                    new ApiStatus(CustomResponseCode.INTERNAL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/check/{todoId}")
    public ResponseEntity<Void> updateTodoChecked(@PathVariable Long todoId) {
        todoCheckService.updateTodoChecked(todoId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/fix/{todoId}")
    public ResponseEntity<Void> updateTodoFixed(@PathVariable Long todoId) {
        todoFixService.updateTodoFixed(todoId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/failtag/{todoId}")
    public ResponseEntity<Void> enrollTodoWithFailtag(@PathVariable Long todoId,
                                                      @RequestBody TodoFailtagRequestDTO requestDTO,
                                                      HttpServletRequest request) {
        todoWithFailtagService.enrollTodoWithFailtag(todoId, requestDTO, request);
        return ResponseEntity.ok().build();
    }
}
