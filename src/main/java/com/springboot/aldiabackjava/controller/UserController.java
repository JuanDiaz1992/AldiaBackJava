package com.springboot.aldiabackjava.controller;


import com.springboot.aldiabackjava.models.User;
import com.springboot.aldiabackjava.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserServices userServices;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userServices.getUser();
    }
}
