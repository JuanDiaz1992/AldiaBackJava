package com.springboot.aldiabackjava.controller;


import com.springboot.aldiabackjava.services.PublicServices;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.BasicUserResponse;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.LoginRequest;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.services.UserServices.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class PublicController {
    @Autowired
    private PublicServices publicServices;


    @PostMapping("/login")
    public ResponseEntity<BasicUserResponse> loginUser(@RequestBody LoginRequest request){
        BasicUserResponse result = publicServices.loginUserService(request);
        if (result != null){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody RegisterRequest request){
        return publicServices.registerUserService(request);
    }
}
