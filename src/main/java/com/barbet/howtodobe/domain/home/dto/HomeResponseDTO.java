package com.barbet.howtodobe.domain.home.dto;

import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Getter
public class HomeResponseDTO {

    private Integer rateOfSuccess;

    @JsonProperty("todoCategoryData")
    private List<TodoCategoryData> todoCategoryData;

    public HomeResponseDTO (Integer rateOfSuccess,
                            List<TodoCategoryData> todoCategoryData,
                            Map<Long, List<TodoCategoryData.TodoData>> todoData) {
        this.rateOfSuccess = rateOfSuccess;
        this.todoCategoryData = todoCategoryData;
        this.todoCategoryData.forEach(categoryData
                -> {
            List<TodoCategoryData.TodoData> todoList = todoData.get(categoryData.getTodoCategoryId());
            if (todoList == null){
                categoryData.todoData = Collections.emptyList();
            }else {
            categoryData.todoData = todoList;
            }
        });
    }

    @Getter
    public static class TodoCategoryData {

        @JsonProperty("todoCategoryId")
        private Long todoCategoryId;

        @JsonProperty("todoData")
        private List<TodoData> todoData;

        @JsonProperty("todoCategory")
        private String todoCategory;


        public TodoCategoryData (Long categoryId, String todoCategory) {
            this.todoCategoryId = categoryId;
            this.todoCategory = todoCategory;
        }

        @Getter
        public static class TodoData {

            @JsonProperty("todoId")
            private Long todoId;


            @JsonProperty("todoName")
            private String todoName;

            @JsonProperty("priority")
            private String priority;

            @JsonProperty("isChecked")
            private Boolean isChecked;

            @JsonProperty("isFixed")
            private Boolean isFixed;

            @JsonProperty("isDelayed")
            private Boolean isDelayed;

            @JsonProperty("failtagName")
            private String failtagName;

            public TodoData (Long todoId,
                             String todoName,
                             String priority,
                             Boolean isChecked,
                             Boolean isFixed,
                             Boolean isDelayed,
                             String failtagName) {
                this.todoId = todoId;
                this.todoName = todoName;
                this.priority = priority;
                this.isChecked = isChecked;
                this.isFixed = isFixed;
                this.isDelayed = isDelayed;
                this.failtagName = failtagName;
            }
        }
    }
}
