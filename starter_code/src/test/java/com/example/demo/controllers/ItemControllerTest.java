package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository",
                itemRepository);

    }

    @Test
    public void getItemByIdTest(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Created Item");
        item.setDescription("This is created item");
        item.setPrice(BigDecimal.valueOf(10.0));
        doReturn(Optional.of(item)).when(itemRepository).findById(1L);
        ResponseEntity<Item> responseEntity =
                itemController.getItemById(1L);
        assertNotNull(responseEntity);
        assertEquals("Created Item", responseEntity.getBody().getName());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameTest(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Created Item");
        item.setDescription("This is created item");
        item.setPrice(BigDecimal.valueOf(10.0));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        doReturn(itemList).when(itemRepository).findByName("Created Item");
        ResponseEntity<List<Item>> responseEntity =
                itemController.getItemsByName("Created Item");
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameTestNullItem(){
        List<Item> itemList = new ArrayList<>();
        doReturn(null).when(itemRepository).findByName("Created Item");

        ResponseEntity<List<Item>> responseEntity =
                itemController.getItemsByName("Created Item");
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
    @Test
    public void getAllItemsTest(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Created Item");
        item.setDescription("This is created item");
        item.setPrice(BigDecimal.valueOf(10.0));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        doReturn(itemList).when(itemRepository).findAll();
        ResponseEntity<List<Item>> responseEntity =
                itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

    }







}
