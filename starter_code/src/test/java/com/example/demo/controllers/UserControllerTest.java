package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;


public class UserControllerTest {

    private UserController userController;

    private User testUser;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder =
            mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository",
                userRepository);
        TestUtils.injectObject(userController, "cartRepository",
                cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder",
                bCryptPasswordEncoder);
    }

    @Test
    public void test_create_user() {
        User user = createUser();
        CreateUserRequest rq = new CreateUserRequest("usertest", "lalalala",
               "lalalala");
        ResponseEntity<User> createdUser = userController.createUser(rq);
        assertNotNull(createdUser);
        assertEquals( 201 ,createdUser.getStatusCodeValue());
    }
    @Test
    public void createUserTest(){

    }



    public static Cart createCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<Item>());
        cart.setTotal(BigDecimal.valueOf(0.0));
        return cart;
    }

    public static User createUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("Password");
        user.setCart(createCart());
        return user;
    }


}



