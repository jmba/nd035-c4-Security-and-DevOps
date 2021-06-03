package com.example.demo;

import com.example.demo.model.requests.CreateUserRequest;

import java.lang.reflect.Field;

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
}
