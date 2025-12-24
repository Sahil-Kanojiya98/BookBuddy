package com.example.security.authentication;

import com.example.config.JwtTokenConfigProperties;
import com.example.exception.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenService {

    private final Key key;
    private final long EXPIRATION_MS;

    private final String USERNAME_CLAIM = "username";
    private final String EMAIL_CLAIM = "email";
    private final String AUTHORITIES_CLAIM = "authorities";

    public JwtTokenService(JwtTokenConfigProperties jwtTokenConfigProperties){
        key = Keys.hmacShaKeyFor(jwtTokenConfigProperties.getSecret().getBytes());
        EXPIRATION_MS = jwtTokenConfigProperties.getExpirationMs();
    }

    public String generateToken(Long userId, String username, String email, List<String> authorities) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim(USERNAME_CLAIM, username)
                .claim(EMAIL_CLAIM, email)
                .claim(AUTHORITIES_CLAIM, authorities)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtPayload verify(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.valueOf(claims.getSubject());
            String username = claims.get(USERNAME_CLAIM, String.class);
            String email = claims.get(EMAIL_CLAIM, String.class);

            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) claims.get(AUTHORITIES_CLAIM);

            return new JwtPayload(userId, username, email, authorities);

        } catch (Exception e) {
            throw new InvalidJwtTokenException("Invalid JWT token", e);
        }
    }
}
