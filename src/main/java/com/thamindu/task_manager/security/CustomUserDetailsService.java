package com.thamindu.task_manager.security;

import com.thamindu.task_manager.exception.UsernameNotFoundException;
import com.thamindu.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                            log.warn("User authentication failed. Username not found: {}", username);
                            return new UsernameNotFoundException("User not found with username");
                        }
                        );
    }
}
