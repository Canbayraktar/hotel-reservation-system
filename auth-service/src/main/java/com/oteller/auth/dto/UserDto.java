package com.oteller.auth.dto;

import com.oteller.auth.model.Role;
import lombok.Data;

/**
 * Kullanıcı DTO sınıfı.
 * Servis katmanı ile sunum katmanı arasında veri transferi için kullanılır.
 */
@Data
public class UserDto {
    private Long id;
    private String username;
    private Role role;
} 