package com.oteller.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oteller.auth.dto.LoginRequest;
import com.oteller.auth.dto.LoginResponse;
import com.oteller.auth.dto.RegisterRequest;
import com.oteller.auth.dto.UserDto;
import com.oteller.auth.exception.AuthenticationFailedException;
import com.oteller.auth.exception.UserAlreadyExistsException;
import com.oteller.auth.mapper.UserMapper;
import com.oteller.auth.model.Role;
import com.oteller.auth.model.User;
import com.oteller.auth.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDto register(RegisterRequest request) {
        if (userService.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(request.getUsername()))) {
            throw new UserAlreadyExistsException("Username already exists.");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        
        User savedUser = userService.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationFailedException("Invalid username or password");
        }

        String token = jwtTokenProvider.generateToken(user);
        
        LoginResponse response = new LoginResponse();
        response.setAccessToken(token);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        
        return response;
    }
} 