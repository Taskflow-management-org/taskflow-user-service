package com.thamindu.task_manager.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record AuthResponseDto(
        String token,
        String tokenType,
        String username,
        Set<String> roles
) {
}