package com.springboot.aldiabackjava.services;

import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.IUserRepository;
import com.springboot.aldiabackjava.utils.EncryptPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServices {
    private static final Logger log = LoggerFactory.getLogger(UserServices.class);
    @Autowired
    private IUserRepository iUserRepository;




    public List<User> getAllUsers(){
        Iterable <User> result = iUserRepository.findAll();
        List <User> users = new ArrayList<>();
        result.forEach(user -> {
            users.add(user);
        });
        return users;
    }

    public User getUser(String username){
        User user = iUserRepository.findByUsername(username);
        return user;
    }

    public String login(Map<String, String> dataLogin) {
        User user = iUserRepository.findByUsername(dataLogin.get("username"));
        if (user!=null){
            Boolean isCorrect = EncryptPassword.validatePassword(dataLogin.get("password"),user.getPassword());
            if (isCorrect){
                return user.getUsername();
            }
        }{
            return null;
        }

    }
}
