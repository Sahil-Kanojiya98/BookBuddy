package com.example.controller;

import com.example.dto.request.LoginUserRequest;
import com.example.dto.request.RegisterUserRequest;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.LoginUserResponse;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> registerUser(
			@Valid @RequestBody RegisterUserRequest registerUserRequest) {
		log.info("Register request received for email: {}", registerUserRequest.getEmail());
		authService.registerUser(registerUserRequest);
		return ApiResponse.build(HttpStatus.CREATED, "User created");
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginUserResponse>> loginUser(
			@Valid @RequestBody LoginUserRequest loginUserRequest) {
		log.info("Register request received for email: {} username: {}", loginUserRequest.getEmail(),
				loginUserRequest.getUsername());
		LoginUserResponse loginUserResponse = authService.loginUser(loginUserRequest);
		return ApiResponse.build(HttpStatus.OK, "Login successful", loginUserResponse);
	}
}
