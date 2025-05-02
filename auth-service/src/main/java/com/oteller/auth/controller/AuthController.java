package com.oteller.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oteller.auth.dto.LoginRequest;
import com.oteller.auth.dto.LoginResponse;
import com.oteller.auth.dto.RegisterRequest;
import com.oteller.auth.dto.UserDto;
import com.oteller.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Kimlik Doğrulama", description = "Kullanıcı kayıt ve giriş işlemleri")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Yeni kullanıcı kaydı", description = "Kullanıcı bilgileri ile yeni hesap oluşturur")
    @ApiResponse(responseCode = "201", description = "Başarılı kayıt")
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) {
        UserDto registeredUser = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
    
    @Operation(summary = "Kullanıcı girişi", description = "Kullanıcı adı ve şifre ile giriş yaparak JWT token alır")
    @ApiResponse(responseCode = "200", description = "Başarılı giriş")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}