package com.barbet.howtodobe.domain.todo.api;

import com.barbet.howtodobe.domain.calendar.application.UpdateSuccessRateService;
import com.barbet.howtodobe.domain.todo.application.TodoAssignService;
import com.barbet.howtodobe.domain.todo.application.TodoCheckService;
import com.barbet.howtodobe.domain.todo.application.TodoFixService;
import com.barbet.howtodobe.domain.todo.application.TodoWithFailtagService;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.barbet.howtodobe.domain.todo.dto.*;
import com.barbet.howtodobe.global.common.response.ApiStatus;
import com.barbet.howtodobe.global.common.response.HowTodoStatus;
import com.barbet.howtodobe.global.common.response.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/fix", produces = "application/json")
    public ResponseEntity<Message> fix(@RequestBody TodoFixRequestDTO todoFixRequestDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            boolean newIsFixed = todoFixService.fixTodo(todoFixRequestDTO);
            String responseMessage;
            if (newIsFixed){
                responseMessage = "투두 고정 완료";
            }
            else {
                responseMessage = "투두 고정 해제 완료";
            }

            TodoFixResponseDTO todoFixResponse = new TodoFixResponseDTO(newIsFixed);

            return new ResponseEntity(
                    new Message(
                    new ApiStatus(HowTodoStatus.OK, responseMessage),
                    todoFixResponse),
                    httpHeaders, HttpStatus.OK);

        }catch (RuntimeException e) {
            return new ResponseEntity(
                    new ApiStatus(HowTodoStatus.INTERNEL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/check", produces = "application/json")
    public ResponseEntity<Message> check(@RequestBody TodoCheckRequestDTO todoCheckRequestDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            boolean newIsChecked = todoCheckService.checkTodo(todoCheckRequestDTO);
            String responseMessage;
            if (newIsChecked){
                responseMessage = "투두 체크 완료";
            }
            else {
                responseMessage = "투두 체크 해제 완료";
            }

            TodoCheckResponseDTO todoCheckResponse = new TodoCheckResponseDTO(newIsChecked);

            return new ResponseEntity(
                    new Message(
                            new ApiStatus(HowTodoStatus.OK, responseMessage),
                            todoCheckResponse),
                    httpHeaders, HttpStatus.OK);

        }catch (RuntimeException e) {
            return new ResponseEntity(
                    new ApiStatus(HowTodoStatus.INTERNEL_SERVER_ERROR, e.getMessage()),
                    httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 아니지 requestBody로 failtagName이랑 isDeley값 보내줘야함
    @PostMapping("/failtag/{todoId}")
    public ResponseEntity<Void> enrollTodoWithFailtag(@PathVariable Long todoId,
                                                      @RequestBody TodoFailtagRequestDTO request) {
        todoWithFailtagService.enrollTodoWithFailtag(todoId, request);
        return ResponseEntity.ok().build();
    }
}
