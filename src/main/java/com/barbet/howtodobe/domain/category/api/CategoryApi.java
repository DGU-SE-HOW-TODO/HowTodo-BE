package com.barbet.howtodobe.domain.category.api;


import com.barbet.howtodobe.domain.category.application.CategoryService;
import com.barbet.howtodobe.domain.category.dto.CategoryRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class CategoryApi {

    private final CategoryService categoryService;

    /** 대분류 등록 */
    @PostMapping("/todoCategory")
    public ResponseEntity<Void> createCategory(@RequestBody CategoryRequestDTO request) {
        categoryService.createCategory(request);
        return ResponseEntity.ok().build();
    }
}
