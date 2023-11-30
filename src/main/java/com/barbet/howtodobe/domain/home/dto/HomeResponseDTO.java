package com.barbet.howtodobe.domain.home.dto;

import com.barbet.howtodobe.domain.member.domain.Member;
import com.barbet.howtodobe.domain.todo.domain.Todo;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class HomeResponseDTO {

    private Integer rateOfSuccess;
    private List<TodoCategoryData> todoCategoryData;
    private LocalDate todayDate;
    private LocalDate selectedDate;

    public static class TodoCategoryData {
        private Long todoCategoryId;
        private List<TodoData> todoData;

        public TodoCategoryData (Long categoryId, List<TodoData> todoDataList) {
            this.todoCategoryId = categoryId;
            this.todoData = todoDataList;
        }

        public static class TodoData {
            private Long todoId;
            private String todoCategory;
            private String todo;
            private String priority;
            private Boolean isChecked;
            private Boolean isFixed;
            private String failtag;
        }
    }

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
}
