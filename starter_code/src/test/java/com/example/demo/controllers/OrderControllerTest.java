package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
public class OrderControllerTest {

    private OrderController orderController;

    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository",
                userRepository);
        TestUtils.injectObject(orderController, "orderRepository",
                orderRepository);

    }

    @Test
    public void submitTest(){
        User user = createUser();
        Item item = new Item();
        item.setId(1L);
        item.setName("Created Item");
        item.setDescription("This is created item");
        item.setPrice(BigDecimal.valueOf(10.0));

        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);
        doReturn(user).when(userRepository).findByUsername("Username");

        ResponseEntity<UserOrder> responseEntity =
                orderController.submit("Username");
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Username", responseEntity.getBody().getUser().getUsername());
    }

    @Test
    public void submitNotFoundUserTest(){
        ResponseEntity<UserOrder> responseEntity =
                orderController.submit("Username");
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserTest(){
        User user = createUser();
        Item item = new Item();
        item.setId(1L);
        item.setName("Created Item");
        item.setDescription("This is created item");
        item.setPrice(BigDecimal.valueOf(10.0));

        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);
        doReturn(user).when(userRepository).findByUsername("Username");
        ResponseEntity<List<UserOrder>> responseEntity =
                orderController.getOrdersForUser("Username");
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

    }

    @Test
    public void getOrdersForUserNotFoundUserTest(){
        ResponseEntity<List<UserOrder>> responseEntity =
                orderController.getOrdersForUser("Username");
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());

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
