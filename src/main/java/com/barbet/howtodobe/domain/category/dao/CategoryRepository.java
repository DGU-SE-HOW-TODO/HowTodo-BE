package com.barbet.howtodobe.domain.category.dao;

import com.barbet.howtodobe.domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
