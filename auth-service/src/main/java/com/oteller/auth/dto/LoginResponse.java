package com.oteller.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    
    @Builder.Default
    private String tokenType = "Bearer";
    
    private String username;
    private String role;
} 