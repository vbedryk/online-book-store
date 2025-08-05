package com.example.bookstore.security;

import com.example.bookstore.dto.user.UserLoginRequestDto;
import com.example.bookstore.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto request);
    Long getCurrentUserId();
}
