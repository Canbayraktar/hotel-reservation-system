package com.oteller.auth.dto;

import com.oteller.auth.model.Role;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private Role role;
} 