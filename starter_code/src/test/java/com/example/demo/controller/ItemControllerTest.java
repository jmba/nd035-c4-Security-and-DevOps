package com.example.demo.controller;

import com.example.demo.Helper;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    String itemName = "item1";
    BigDecimal price = new BigDecimal("22");
    String description = "description";
    int quantity = 5;

    @Before
    public void init() {
        itemController = new ItemController();
        Helper.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemByIdTest() {
        Item item1 = Helper.getDummyItem(1L, itemName, price, description);
        Mockito.when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals(itemName, item.getName());
        assertEquals(Long.valueOf(1),item.getId());
    }

    @Test
    public void getItemsByName() {
        Item item1 = Helper.getDummyItem(1L, itemName, price, description);
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(Arrays.asList(new Item[]{item1}));

        ResponseEntity<List<Item>> response = itemController.getItemsByName(item1.getName());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(itemName, items.get(0).getName());
        assertEquals(Long.valueOf(1), items.get(0).getId());
    }
}
