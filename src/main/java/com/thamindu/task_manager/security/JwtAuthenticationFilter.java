package com.thamindu.task_manager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwtToke;
            String username = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")){
                jwtToke = authHeader.substring(7);

                if (jwtProvider.validationToken(jwtToke)){
                    username = jwtProvider.extractUsername(jwtToke);
                }

            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Successfully authenticated user: {} and set SecurityContext", username);

            }

        }catch (Exception e){
            log.error("Cannot set user authentication in SecurityContext: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
