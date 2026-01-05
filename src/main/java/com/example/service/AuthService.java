package com.example.service;

import com.example.dto.request.LoginUserRequest;
import com.example.dto.request.RegisterUserRequest;
import com.example.dto.response.LoginUserResponse;
import com.example.exception.EmailAlreadyExistsException;
import com.example.exception.UserBookNotFoundException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.authentication.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenService jwtTokenService;

	private static final String DEFAULT_USER_ROLE = "ROLE_USER";

	@Transactional
	public void registerUser(RegisterUserRequest registerUserRequest) {
		if (userRepository.existsByEmail(registerUserRequest.getEmail()))
			throw new EmailAlreadyExistsException(
					String.format("Email already exists. email: %s", registerUserRequest.getEmail()));

		if (userRepository.existsByUsername(registerUserRequest.getUsername()))
			throw new UsernameAlreadyExistsException(
					String.format("Username already exists. username: %s", registerUserRequest.getUsername()));

		User user = new User();
		user.setEmail(registerUserRequest.getEmail());
		user.setUsername(registerUserRequest.getUsername());
		String passwordHash = passwordEncoder.encode(registerUserRequest.getPassword());
		user.setPasswordHash(passwordHash);
		User savedUser = userRepository.save(user);
		log.debug("User saved. id: {}", savedUser.getId());
	}

	@Transactional
	public LoginUserResponse loginUser(LoginUserRequest loginUserRequest) {
		log.info("Login attempt for email: {} or username: {}", loginUserRequest.getEmail(),
				loginUserRequest.getUsername());

		String username;
		if (loginUserRequest.getEmail() != null && !loginUserRequest.getEmail().trim().isEmpty()) {
			Optional<String> optionalUsername = userRepository.findUsernameByEmail(loginUserRequest.getEmail());
			if (optionalUsername.isEmpty())
				throw new BadCredentialsException("Invalid email/username or password");
			else
				username = optionalUsername.get();
		} else
			username = loginUserRequest.getUsername();

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				loginUserRequest.getPassword());
		authenticationManager.authenticate(authenticationToken);

		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isEmpty())
			throw new UserBookNotFoundException("User not found.");

		User user = optionalUser.get();
		String token = jwtTokenService.generateToken(user.getId(), user.getUsername(), user.getEmail(),
				List.of(DEFAULT_USER_ROLE));
		log.debug("Token generated successfully. username: {}", username);

		return new LoginUserResponse(token);
	}
}
