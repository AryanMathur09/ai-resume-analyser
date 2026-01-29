package com.aryan.resume_analyzer.controller;

import com.aryan.resume_analyzer.dto.LoginRequest;
import com.aryan.resume_analyzer.dto.RegisterRequest;
import com.aryan.resume_analyzer.service.UserService;
import com.aryan.resume_analyzer.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return "User registered successfully";
    }
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
