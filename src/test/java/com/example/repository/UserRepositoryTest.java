package com.example.repository;

import com.example.model.User;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Optional;

@DataJpaTest
@Testcontainers
class UserRepositoryTest {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScripts("db/schema.sql", "db/data.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@gmail.com");
        user.setPasswordHash("##pass");

        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("alice");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("alice", foundUser.get().getUsername());
    }
}
