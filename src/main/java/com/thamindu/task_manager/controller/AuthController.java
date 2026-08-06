package com.thamindu.task_manager.controller;

import com.thamindu.task_manager.dto.request.LoginRequestDto;
import com.thamindu.task_manager.dto.request.RegisterRequestDto;
import com.thamindu.task_manager.dto.response.AuthResponseDto;
import com.thamindu.task_manager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/visitor/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto request){
        String response = this.authService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/visitor/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        AuthResponseDto response = authService.loginUser(request);
        return ResponseEntity.ok(response);
    }

}
