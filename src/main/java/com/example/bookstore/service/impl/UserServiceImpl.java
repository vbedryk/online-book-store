package com.example.bookstore.service.impl;

import com.example.bookstore.dto.user.UserRegistrationRequestDto;
import com.example.bookstore.dto.user.UserResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.RegistrationException;
import com.example.bookstore.mapper.UserMapper;
import com.example.bookstore.model.Role;
import com.example.bookstore.model.RoleName;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.role.RoleRepository;
import com.example.bookstore.repository.user.UserRepository;
import com.example.bookstore.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException(String.format("Email %s has already been used",
                    userRegistrationRequestDto.getEmail()));
        }
        User user = userMapper.toModel(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        Role userRole = roleRepository.findRoleByName(RoleName.USER).orElseThrow(
                () -> new EntityNotFoundException("Can't find role for user: "
                        + user.getUsername() + ", role: " + RoleName.USER));
        user.setRoles(Set.of(userRole));
        return userMapper.toDto(userRepository.save(user));
    }
}
