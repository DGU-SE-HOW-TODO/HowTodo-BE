package com.barbet.howtodobe.domain.todo.dao;

import com.barbet.howtodobe.domain.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
