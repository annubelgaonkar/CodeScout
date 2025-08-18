package dev.anuradha.githubreposearcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private boolean success;
    private String message;
    private int status;
    private LocalDateTime timestamp;
}
