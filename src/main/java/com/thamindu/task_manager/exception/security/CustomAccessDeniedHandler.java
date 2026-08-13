package com.thamindu.task_manager.exception.security;

import com.thamindu.task_manager.util.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, @NonNull AccessDeniedException accessDeniedException) throws IOException {
        log.warn("Forbidden access attempt on path: {} by user", request.getRequestURI());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponseDto errorPayload =  new ErrorResponseDto(
                Instant.now(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Forbidden Access",
                "You do not have the required permissions to perform this operation.",
                request.getRequestURI()
        );

        objectMapper.writeValue(response.getOutputStream(), errorPayload);
    }
}















