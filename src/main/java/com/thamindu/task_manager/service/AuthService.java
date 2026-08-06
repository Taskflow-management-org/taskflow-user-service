package com.thamindu.task_manager.service;

import com.thamindu.task_manager.dto.request.LoginRequestDto;
import com.thamindu.task_manager.dto.request.RegisterRequestDto;
import com.thamindu.task_manager.dto.response.AuthResponseDto;

public interface AuthService {
    public String registerUser(RegisterRequestDto request);
    public AuthResponseDto loginUser(LoginRequestDto request);
}
