package com.thamindu.task_manager.exception.security;

import com.thamindu.task_manager.util.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.awt.*;
import java.io.IOException;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull AuthenticationException authException) throws IOException {
        log.error("Unauthorized error occurred on path: {}. Message: {}", request.getRequestURI(), authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

       ErrorResponseDto errorPayload =  new ErrorResponseDto(
                Instant.now(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized Access",
                "You must be authenticated to access this resource",
                request.getRequestURI()
        );

       objectMapper.writeValue(response.getOutputStream(), errorPayload);
    }
}















