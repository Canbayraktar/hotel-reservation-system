package com.oteller.auth.service;

import com.oteller.auth.dto.LoginRequest;
import com.oteller.auth.dto.LoginResponse;
import com.oteller.auth.dto.RegisterRequest;
import com.oteller.auth.dto.UserDto;
import com.oteller.auth.exception.AuthenticationFailedException;
import com.oteller.auth.exception.UserAlreadyExistsException;

public interface AuthService {
    UserDto register(RegisterRequest request);    
    LoginResponse login(LoginRequest request);
}