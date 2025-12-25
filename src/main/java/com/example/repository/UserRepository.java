package com.example.repository;

import com.example.dto.UserPrincipalDto;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT new com.example.dto.UserPrincipalDto(u.id, u.username, u.email, u.passwordHash) " +
            "FROM User u " +
            "WHERE u.username = :username")
    Optional<UserPrincipalDto> findUserPrincipalByUsername(@Param("username") String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u.username " +
            "FROM User u " +
            "WHERE u.email = :email")
    Optional<String> findUsernameByEmail(@Param("email") String email);

    Optional<User> findByUsername(String username);
}
