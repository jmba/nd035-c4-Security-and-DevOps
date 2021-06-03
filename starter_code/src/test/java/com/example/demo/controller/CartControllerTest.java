package com.example.demo.controller;

import com.example.demo.Helper;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;

public class CartControllerTest {
    private CartController cartController;
    private ItemController itemController;
    private OrderController orderController;
    private UserController userController;

    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void init() {
        Helper.injectObjects(cartController, "userRepository", userRepository);
        Helper.injectObjects(cartController, "cartRepository", cartRepository);
        Helper.injectObjects(cartController, "itemRepository", itemRepository);
        Helper.injectObjects(itemController, "itemRepository", itemRepository);
        Helper.injectObjects(orderController, "userRepository", userRepository);
        Helper.injectObjects(orderController, "orderRepository", orderRepository);
        Helper.injectObjects(userController, "cartRepository", cartRepository);
        Helper.injectObjects(userController, "userRepository", userRepository);
        Helper.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }
}
