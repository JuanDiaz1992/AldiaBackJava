package com.springboot.aldiabackjava.controller;

import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Demo {
    @Autowired
    private AuthService authService;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return authService.getAllUsers();
    }
    @GetMapping("/users/{username}")
    public User getUserForUserName(@PathVariable String username){
        return  authService.getUser(username);
    }
}
