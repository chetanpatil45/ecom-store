package com.ecom.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {
    private String error;
    private int status;
    private LocalDateTime timestamp;
}
