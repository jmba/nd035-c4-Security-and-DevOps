package com.example.demo.controller;

import com.example.demo.Helper;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class OderControllerTest {
    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    String userName = "Johnson";
    String password = "password123";
    String itemName = "item1";
    BigDecimal price = new BigDecimal("22");
    String description = "description";
    int quantity = 5;

    @Before
    public void init() {
        orderController = new OrderController();
        Helper.injectObjects(orderController, "userRepository", userRepository);
        Helper.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitTest() {
        Item item1 = Helper.getDummyItem(1L, itemName, price, description);
        User user1 = Helper.getDummyUser(1l, userName, password, new Cart());
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        Cart cart = Helper.getDummyCart(1L, itemList ,user1);
        user1.setCart(cart);

        Mockito.when(userRepository.findByUsername(userName)).thenReturn(user1);

        ResponseEntity<UserOrder> responseEntity = orderController.submit(user1.getUsername());
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());
        UserOrder order = responseEntity.getBody();
        assertNotNull(order);
        assertEquals(1,order.getItems().size());
    }

    @Test
    public void submitNotFoundTest() {
        Mockito.when(userRepository.findByUsername("Unknown")).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("Unknown");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getOrdersForUserTest() {
        Item item1 = Helper.getDummyItem(1L, itemName, price, description);
        User user1 = Helper.getDummyUser(1l, userName, password, new Cart());
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        Cart cart = Helper.getDummyCart(1L, itemList ,user1);
        user1.setCart(cart);

        Mockito.when(userRepository.findByUsername(userName)).thenReturn(user1);
        orderController.submit(user1.getUsername());

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(user1.getUsername());
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());

        List<UserOrder> orders = responseEntity.getBody();
        assertNotNull(orders);
    }

    @Test
    public void getOrdersForUserNotFoundTest() {
        Mockito.when(userRepository.findByUsername("Unknown")).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Unknown");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
