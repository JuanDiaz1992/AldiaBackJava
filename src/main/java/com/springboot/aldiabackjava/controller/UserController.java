package com.springboot.aldiabackjava.controller;


import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserServices userServices;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userServices.getAllUsers();
    }
    @GetMapping("/users/{username}")
    public User getUserForUserName(@PathVariable String username){
        return  userServices.getUser(username);
    }
    @PostMapping("/users/login")
    public String loginUser(@RequestBody Map<String, String> dataLogin){
        return userServices.login(dataLogin);
    }
}
