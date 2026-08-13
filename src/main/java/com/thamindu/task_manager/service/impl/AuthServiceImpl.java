package com.thamindu.task_manager.service.impl;

import com.thamindu.task_manager.dto.request.LoginRequestDto;
import com.thamindu.task_manager.dto.request.RegisterRequestDto;
import com.thamindu.task_manager.dto.response.AuthResponseDto;
import com.thamindu.task_manager.entity.User;
import com.thamindu.task_manager.exception.UserAlreadyExistsException;
import com.thamindu.task_manager.repository.UserRepository;
import com.thamindu.task_manager.security.JwtProvider;
import com.thamindu.task_manager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public String registerUser(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
         throw new UserAlreadyExistsException("Email is already registered!");
        }

        User newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .isEnable(true)
                .roles(Set.of("ROLE_USER"))
                .build();

        userRepository.save(newUser);
        return "User registered successfully!";

    }

    @Override
    @Transactional
    public AuthResponseDto loginUser(LoginRequestDto request) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.username(), request.password()
        );

        Authentication authentication = authenticationManager.authenticate(authToken);

        String jwtToken = jwtProvider.generateToken(authentication);


        UserDetails userPrincipal =  (UserDetails) authentication.getPrincipal();
        assert userPrincipal != null;
        Set<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return AuthResponseDto.builder()
                .token(jwtToken)
                .roles(roles)
                .username(userPrincipal.getUsername())
                .build();
    }
}
