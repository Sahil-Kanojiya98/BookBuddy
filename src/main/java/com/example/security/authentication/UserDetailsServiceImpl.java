package com.example.security.authentication;

import com.example.dto.UserPrincipalDto;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserPrincipalDto> optionalUserPrincipalDto = userRepository.findUserPrincipalByUsername(username);

        UserPrincipalDto userPrincipalDto = optionalUserPrincipalDto.orElseThrow(() ->
                new UsernameNotFoundException(String.format("User not found with username: %s", username))
        );

        return new UserPrincipal(
                userPrincipalDto.getId(),
                userPrincipalDto.getUsername(),
                userPrincipalDto.getEmail(),
                List.of(new SimpleGrantedAuthority(DEFAULT_USER_ROLE))
        );
    }
}
