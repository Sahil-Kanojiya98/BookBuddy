package com.example.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final RequestMatcher requestMatcher;
    private final JwtTokenService jwtTokenService;

    public JwtTokenAuthenticationFilter(RequestMatcher matcher, JwtTokenService jwtTokenService) {
        this.requestMatcher = matcher;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getServletPath();
        log.info("Incoming request path: {}", path);

        if (requestMatcher.matches(request)) {
            try {
                Authentication auth = attemptAuthentication(request, response);
                if (auth != null) {
                    successfulAuthentication(request, response, filterChain, auth);
                    return;
                }
            } catch (Exception e) {
                unsuccessfulAuthentication(request, response, null);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String header = obtainJwtTokenHeader(request);
        log.info("Attempting Authentication for header {}", header);

        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            return null;
        }

        String token = header.substring(7);

        JwtPayload payload = jwtTokenService.verify(token);

        UserPrincipal principal = new UserPrincipal(payload.getUserId(), payload.getUsername(), payload.getEmail(), payload.getAuthorities());

        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    protected String obtainJwtTokenHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.debug("Setting security context with principal: {}", authResult.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.debug("Clearing security context and returning 401 Unauthorized.");
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
