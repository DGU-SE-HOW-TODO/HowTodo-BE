package com.barbet.howtodobe.domain.failtag.api;

import com.barbet.howtodobe.domain.failtag.application.FailtagService;
import com.barbet.howtodobe.domain.failtag.dto.FailtagRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/failtag")
public class FailtagApi {

    private final FailtagService failtagService;

    @PostMapping("/todoCategory")
    public ResponseEntity<Void> select5Failtag(@RequestBody FailtagRequestDTO request) {
        failtagService.select5Failtags(request);
        return ResponseEntity.ok().build();
    }
}
