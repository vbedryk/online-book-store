package com.example.bookstore.security;

import com.example.bookstore.dto.user.UserLoginRequestDto;
import com.example.bookstore.dto.user.UserLoginResponseDto;
import com.example.bookstore.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }

    @Override
    public Long getCurrentUserId() {
        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getCredentials();
        return currentUser.getId();
    }
}
