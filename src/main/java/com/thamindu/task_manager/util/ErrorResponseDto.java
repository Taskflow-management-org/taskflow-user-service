package com.thamindu.task_manager.util;

import java.time.Instant;

public record ErrorResponseDto(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
