package com.example.service;

import com.example.dto.request.LoginUserRequest;
import com.example.dto.request.RegisterUserRequest;
import com.example.dto.response.BookResponse;
import com.example.dto.response.LoginUserResponse;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public void registerUser(RegisterUserRequest registerUserRequest) {
        BookResponse bookResponse = new BookResponse();
    }

    @Transactional
    public LoginUserResponse loginUser(LoginUserRequest loginUserRequest) {
        return null;
    }
}
