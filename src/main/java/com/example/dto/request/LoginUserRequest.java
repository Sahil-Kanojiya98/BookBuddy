package com.example.dto.request;

import com.example.validation.ExactlyOneLoginIdentifier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ExactlyOneLoginIdentifier(message = "Either email or username must be provided, but not both")
public class LoginUserRequest {

	@Size(max = 255, message = "Email must not exceed 255 characters")
	private String email;

	@Size(min = 3, max = 32, message = "Username must be between 3 and 32 characters")
	private String username;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
	private String password;
}
