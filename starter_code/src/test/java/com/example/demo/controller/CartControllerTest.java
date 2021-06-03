package com.example.demo.controller;

import com.example.demo.Helper;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.mock;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    String userName = "Johnson";
    String password = "password123";
    String encodedPassword = "encodedPwassword";

    String itemName = "item1";
    BigDecimal price = new BigDecimal("22");
    String description = "description";
    int quantity = 5;

    @Before
    public void init() {
        cartController = new CartController();
        Helper.injectObjects(cartController, "userRepository", userRepository);
        Helper.injectObjects(cartController, "cartRepository", cartRepository);
        Helper.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCartTest() {
        User user1 = Helper.getDummyUser(1l, userName, password, new Cart());
        Item item1 = Helper.getDummyItem(1L, itemName, price, description);
        Mockito.when(userRepository.findByUsername(userName)).thenReturn(user1);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        ModifyCartRequest newCartRequest = Helper.getDummyCartRequest(1L, quantity, userName);
        ArrayList<Item> listOfItems = new ArrayList<Item>();
        listOfItems.add(item1);

        ResponseEntity<Cart> response = cartController.addTocart(newCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(quantity, cart.getItems().size());
    }

    @Test
    public void removeFromCartTest() {
        User user1 = Helper.getDummyUser(1l, userName, password, new Cart());
        Item item1 = Helper.getDummyItem(1L, itemName, price, description);
        Mockito.when(userRepository.findByUsername(userName)).thenReturn(user1);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        ModifyCartRequest cartRequest = Helper.getDummyCartRequest(1L, quantity, userName);
        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
    }
}
