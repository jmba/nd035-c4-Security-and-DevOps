package com.example.demo.controller;

import com.example.demo.Helper;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class UserControllerTest {

    private UserController userController;

    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void init() {
        userController = new UserController();
        Helper.injectObjects(userController, "cartRepository", cartRepository);
        Helper.injectObjects(userController, "userRepository", userRepository);
        Helper.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void createUserTest() {
        String userName = "Johnson";
        String password = "password123";
        String encodedPassword = "encodedPwassword";

        CreateUserRequest createUserRequest = Helper.getDummyCreateUserRequest(userName, password);

        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn(encodedPassword);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        User userCreated = response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals( userName, userCreated.getUsername() );
        assertEquals(encodedPassword, userCreated.getPassword());
    }

    @Test
    public void findByUserNameTest() {
        String userName = "Johnson";
        String password = "password123";
        CreateUserRequest createUserRequest = Helper.getDummyCreateUserRequest(userName, password);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(200,response.getStatusCodeValue());
        User userCreated = response.getBody();
        Mockito.when(userRepository.findByUsername(userCreated.getUsername())).thenReturn(userCreated);

        ResponseEntity<User> foundUser = userController.findByUserName(userName);
        assertEquals(userName , foundUser.getBody().getUsername());
        assertEquals(200,foundUser.getStatusCodeValue());
    }

    @Test
    public void findByIdTest() {
        String userName = "Johnson";
        String password = "password123";
        CreateUserRequest createUserRequest = Helper.getDummyCreateUserRequest(userName, password);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(200,response.getStatusCodeValue());
        User userCreated = response.getBody();
        Mockito.when(userRepository.findById(userCreated.getId())).thenReturn(Optional.of(userCreated));

        ResponseEntity<User> foundUser = userController.findById(userCreated.getId());
        assertEquals(userName , foundUser.getBody().getUsername());
        assertEquals(200,foundUser.getStatusCodeValue());
    }
}
