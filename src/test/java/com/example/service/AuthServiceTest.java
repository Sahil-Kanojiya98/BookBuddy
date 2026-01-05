package com.example.service;

import com.example.dto.request.LoginUserRequest;
import com.example.dto.request.RegisterUserRequest;
import com.example.dto.response.LoginUserResponse;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.authentication.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	UserRepository userRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@Mock
	AuthenticationManager authenticationManager;

	@Mock
	JwtTokenService jwtTokenService;

	@InjectMocks
	AuthService authService;

	@Test
	void registerUser() {
		String email = "sahil@gmail.com";
		String username = "sahil";
		String password = "sahil@123";

		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setEmail(email);
		registerUserRequest.setUsername(username);
		registerUserRequest.setPassword(password);

		when(userRepository.existsByEmail(email)).thenReturn(false);
		when(userRepository.existsByUsername(username)).thenReturn(false);
		when(passwordEncoder.encode(password)).thenReturn("###HashedPassword###");
		User user = new User();
		user.setId(1L);
		when(userRepository.save(any(User.class))).thenReturn(user);

		authService.registerUser(registerUserRequest);

		verify(userRepository, times(1)).existsByEmail(email);
		verify(userRepository, times(1)).existsByUsername(username);
		verify(passwordEncoder, times(1)).encode(password);
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void loginUser() {
		Long userId = 123L;
		String email = "sahil@gmail.com";
		String password = "sahil@123";
		String username = "sahil";

		when(userRepository.findUsernameByEmail(email)).thenReturn(Optional.of(username));

		var tokenCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
		var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, List.of());
		when(authenticationManager.authenticate(tokenCaptor.capture())).thenReturn(usernamePasswordAuthenticationToken);

		User user = new User();
		user.setId(userId);
		user.setUsername(username);
		user.setEmail(email);
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		String token = "###Token###";
		when(jwtTokenService.generateToken(eq(userId), eq(username), eq(email), anyList())).thenReturn(token);

		LoginUserRequest loginUserRequest = new LoginUserRequest();
		loginUserRequest.setEmail(email);
		loginUserRequest.setPassword(password);
		LoginUserResponse loginUserResponse = authService.loginUser(loginUserRequest);

		assertEquals(token, loginUserResponse.getToken());

		verify(userRepository, times(1)).findUsernameByEmail(email);
		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userRepository, times(1)).findByUsername(username);
		verify(jwtTokenService, times(1)).generateToken(eq(userId), eq(username), eq(email), anyList());
	}
}