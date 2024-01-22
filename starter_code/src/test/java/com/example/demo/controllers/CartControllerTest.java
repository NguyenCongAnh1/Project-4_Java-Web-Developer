package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import com.example.demo.TestUtils;
import org.springframework.test.context.TestPropertySource;

public class CartControllerTest {

    private CartController cartController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);


    private Item item;

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository",
                userRepository);
        TestUtils.injectObject(cartController, "cartRepository",
                cartRepository);
        TestUtils.injectObject(cartController, "itemRepository",
                itemRepository);

    }


    @Test
    public void addToCartNoUserError() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest("", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addToCartSuccess(){
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
        when(userRepository.findByUsername("tes1")).thenReturn(user);
        doReturn(Optional.of(item)).when(itemRepository).findById(1L);
        ModifyCartRequest modifyCartRequest = createModifyCartRequest("tes1", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void addTocartNotFoundItem(){
        when(userRepository.findByUsername("tes1")).thenReturn(new User());
        doReturn(Optional.empty()).when(itemRepository).findById(1L);
        ModifyCartRequest modifyCartRequest = createModifyCartRequest("tes1", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }


    @Test
    public void removeCartSuccess(){
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

        when(userRepository.findByUsername("tes1")).thenReturn(user);
        doReturn(Optional.of(item)).when(itemRepository).findById(1L);

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("tes1", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeCartNotFoundUser(){
        ModifyCartRequest modifyCartRequest = createModifyCartRequest("", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeCartNotFoundItem(){
        when(userRepository.findByUsername("tes1")).thenReturn(new User());
        doReturn(Optional.empty()).when(itemRepository).findById(1L);
        ModifyCartRequest modifyCartRequest = createModifyCartRequest("tes1", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());

    }



    public static ModifyCartRequest createModifyCartRequest(String username, long itemId, int quantity){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        return modifyCartRequest;
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


