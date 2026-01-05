package com.example.security.authentication;

import com.example.config.JwtTokenConfigProperties;
import com.example.exception.InvalidJwtTokenException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    static JwtTokenService jwtTokenService;

    @BeforeAll
    static void setUp() {
        JwtTokenConfigProperties jwtTokenConfigProperties = new JwtTokenConfigProperties();
        jwtTokenConfigProperties.setSecret("your-256-bit-secret-your-256-bit-secret-your-256-bit-secret");
        jwtTokenConfigProperties.setExpirationMs(86400000);
        jwtTokenService = new JwtTokenService(jwtTokenConfigProperties);
    }

    @Test
    void generateValidTokenAndVerify() {
        Long userId = 123L;
        String username = "sahil";
        String email = "sahil@gmail.com";
        List<String> authorityList = List.of("USER_ROLE");
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = authorityList.stream().map(SimpleGrantedAuthority::new).toList();

        String token = jwtTokenService.generateToken(userId, username, email, authorityList);
        JwtPayload jwtPayload = jwtTokenService.verify(token);

        assertEquals(userId, jwtPayload.getUserId());
        assertEquals(username, jwtPayload.getUsername());
        assertEquals(email, jwtPayload.getEmail());
        assertEquals(simpleGrantedAuthorityList, jwtPayload.getAuthorities());
    }

    @Test
    void generateInvalidTokenAndVerify() {
        Long userId = 123L;
        Long testUserId = 987L;
        String username = "sahil";
        String email = "sahil@gmail.com";
        String testEmail = "sahil@test.com";
        List<String> authorityList = List.of("USER_ROLE");
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = authorityList.stream().map(SimpleGrantedAuthority::new).toList();

        String token = jwtTokenService.generateToken(userId, username, email, authorityList);
        JwtPayload jwtPayload = jwtTokenService.verify(token);

        assertNotEquals(testUserId, jwtPayload.getUserId());
        assertEquals(username, jwtPayload.getUsername());
        assertNotEquals(testEmail, jwtPayload.getEmail());
        assertEquals(simpleGrantedAuthorityList, jwtPayload.getAuthorities());
    }

    @Test
    void verifyTamperedTokenThrowsException() {
        Long userId = 123L;
        String username = "sahil";
        String email = "sahil@gmail.com";
        List<String> authorityList = List.of("USER_ROLE");

        String token = jwtTokenService.generateToken(userId, username, email, authorityList);
        String tamperedToken = token + "USGSKWHISUH";
        assertThrows(InvalidJwtTokenException.class, () -> jwtTokenService.verify(tamperedToken));
    }
}