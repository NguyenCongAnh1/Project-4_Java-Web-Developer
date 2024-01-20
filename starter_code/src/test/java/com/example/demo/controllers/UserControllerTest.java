package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private User testUser;

    @BeforeEach
    public void setUpEach() {
        when(bCryptPasswordEncoder.encode("lalalala")).thenReturn("thisIsHard");
        CreateUserRequest rq = new CreateUserRequest("usertest", "lalalala",
                "lalalala");
        ResponseEntity<User> createdUser = userController.createUser(rq);
        testUser = createdUser.getBody();
    }

    @Test
    public void test_create_user() {
        Assertions.assertNotNull(testUser);
        Assertions.assertEquals("usertest", testUser.getUsername());
        Assertions.assertEquals("thisIsHard", testUser.getPassword());
    }

    @Test
    public void test_findById() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        final ResponseEntity<User> response2 =
                userController.findById(testUser.getId());
        User userFound = response2.getBody();
        Assertions.assertEquals(200, response2.getStatusCodeValue());
        Assertions.assertEquals("usertest", userFound.getUsername());
    }

    @Test
    public void test_findByUserName() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

        final ResponseEntity<User> response2 =
                userController.findByUserName(testUser.getUsername());
        Assertions.assertEquals(200, response2.getStatusCodeValue());
        Assertions.assertEquals("usertest", response2.getBody().getUsername());
    }
}



