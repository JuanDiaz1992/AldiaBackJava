package com.springboot.aldiabackjava.services;

import com.springboot.aldiabackjava.models.User;
import com.springboot.aldiabackjava.repositories.IProfileRepository;
import com.springboot.aldiabackjava.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServices {
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IProfileRepository iProfileRepository;

    public List<User> getUser(){
        Iterable <User> result = iUserRepository.findAll();
        List <User> users = new ArrayList<>();
        result.forEach(user -> {
            users.add(user);
        });
        return users;
    }

}
