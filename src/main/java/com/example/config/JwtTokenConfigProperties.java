package com.example.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.security.jwt-token")
public class JwtTokenConfigProperties {

	private String secret;
	private long expirationMs;
}
