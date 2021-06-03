package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Helper {
    public static void injectObjects(Object target, String fieldName, Object toInject){
        boolean wasPrivate = false;
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            if(!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate = true;
            }
            field.set(target, toInject);
            if(wasPrivate){
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static CreateUserRequest getDummyCreateUserRequest(String userName, String password){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername(userName);
        userRequest.setPassword(password);
        userRequest.setConfirmPassword(password);
        return userRequest;
    }

    public static User getDummyUser(long userId, String username, String password, Cart cart) {
        User newUser = new User();
        newUser.setId(userId);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setCart(cart);
        return newUser;
    }

    public static ModifyCartRequest getDummyCartRequest(long itemId, int quantity, String username) {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(quantity);
        cartRequest.setUsername(username);
        return cartRequest;
    }

    public static Item getDummyItem(Long id, String name, BigDecimal price, String description) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        return item;
    }

    public static Cart getDummyCart(long cartId, ArrayList<Item> items, User user) {
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setItems(items);
        cart.setUser(user);
        return cart;
    }
}
