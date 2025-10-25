package com.tsimerekis.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void registerUser() {
        userService.registerUser();

    }
}