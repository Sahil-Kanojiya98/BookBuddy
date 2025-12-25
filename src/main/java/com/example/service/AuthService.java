package com.example.service;

import com.example.dto.request.LoginUserRequest;
import com.example.dto.request.RegisterUserRequest;
import com.example.dto.response.BookResponse;
import com.example.dto.response.LoginUserResponse;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void registerUser(RegisterUserRequest registerUserRequest) {
        // TODO: Check if user with the same email or username already exists then throw an exception
//        if (userRepository.existsByEmail(registerUserRequest.getEmail())){
//            throw new
//        }
//        if (userRepository.existsByUsername(registerUserRequest.getUsername())){
//
//        }

        User user = new User();
        user.setEmail(registerUserRequest.getEmail());
        user.setUsername(registerUserRequest.getUsername());
        String passwordHash = passwordEncoder.encode(registerUserRequest.getPassword());
        user.setPasswordHash(passwordHash);
        userRepository.save(user);
    }

    @Transactional
    public LoginUserResponse loginUser(LoginUserRequest loginUserRequest) {
        // TODO: Validate data then authenticate user and generate JWT token
        return null;
    }
}
