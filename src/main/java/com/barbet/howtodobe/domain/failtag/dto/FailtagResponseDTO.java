package com.barbet.howtodobe.domain.failtag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FailtagResponseDTO {
    List<String> failtags;
}
