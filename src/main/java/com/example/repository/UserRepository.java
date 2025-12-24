package com.example.repository;

import com.example.dto.UserPrincipalDto;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT new com.example.dto.UserPrincipalDto(u.id, u.username, u.email) " +
            "FROM User u WHERE u.username = :username")
    Optional<UserPrincipalDto> findUserPrincipalByUsername(String username);
}
