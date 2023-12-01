package com.barbet.howtodobe.domain.home.dto;

import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;


@Getter
public class HomeResponseDTO {

    private Integer rateOfSuccess;

    @JsonProperty("todoCategoryData")
    private List<TodoCategoryData> todoCategoryData;

    private LocalDate todayDate;
    private LocalDate selectedDate;

    public HomeResponseDTO (Integer rateOfSuccess,
                            List<TodoCategoryData> todoCategoryData,
                            LocalDate todayDate,
                            LocalDate selectedDate,
                            List<TodoCategoryData.TodoData> todoData) {
        this.rateOfSuccess = rateOfSuccess;
        this.todoCategoryData = todoCategoryData;
        this.todayDate = todayDate;
        this.selectedDate = selectedDate;
        this.todoCategoryData.forEach(categoryData -> categoryData.todoData = todoData);
    }

    @Getter
    public static class TodoCategoryData {

        @JsonProperty("todoCategoryId")
        private Long todoCategoryId;

        @JsonProperty("todoData")
        private List<TodoData> todoData;


        public TodoCategoryData (Long categoryId, List<TodoData> todoDataList) {
            this.todoCategoryId = categoryId;
            this.todoData = todoDataList;
        }

        @Getter
        public static class TodoData {

            @JsonProperty("todoId")
            private Long todoId;

            @JsonProperty("todoCategory")
            private String todoCategory;

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
                             String todoCategory,
                             String todoName,
                             String priority,
                             Boolean isChecked,
                             Boolean isFixed,
                             Boolean isDelayed,
                             String failtagName) {
                this.todoId = todoId;
                this.todoCategory = todoCategory;
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
