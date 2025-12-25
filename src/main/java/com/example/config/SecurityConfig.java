package com.example.config;

import com.example.repository.UserRepository;
import com.example.security.authentication.JwtTokenAuthenticationFilter;
import com.example.security.authentication.JwtTokenService;
import com.example.security.authentication.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final static String[] PRIVATE_ROUTES = {
            "/api/v1/**"
    };

    private final static String[] PUBLIC_ROUTES = {
            "/api/v1/auth/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private final static int PASSWORD_ENCODER_STRENGTH = 5;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter) throws Exception {
        http
                .csrf((Customizer<CsrfConfigurer<HttpSecurity>>) AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ROUTES).permitAll()
                        .requestMatchers(PRIVATE_ROUTES).authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(JwtTokenService jwtTokenService) {
        RequestMatcher[] publicMatchers = Arrays.stream(PUBLIC_ROUTES)
                .map(AntPathRequestMatcher::new)
                .toArray(RequestMatcher[]::new);
        RequestMatcher publicRoutesMatcher = new OrRequestMatcher(publicMatchers);
        RequestMatcher protectedRoutesMatcher = new NegatedRequestMatcher(publicRoutesMatcher);

        return new JwtTokenAuthenticationFilter(protectedRoutesMatcher, jwtTokenService);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        UserDetailsService userDetailsService = new UserDetailsServiceImpl(userRepository);
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(PASSWORD_ENCODER_STRENGTH);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
