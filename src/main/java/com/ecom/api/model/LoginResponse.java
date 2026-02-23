package com.ecom.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private String jwt;
    private boolean success;
    private String failureReason;
}
