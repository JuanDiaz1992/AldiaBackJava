package com.springboot.aldiabackjava.controller;


import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.BasicUserResponse;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.LoginRequest;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.services.UserServices.AuthService;
import lombok.RequiredArgsConstructor;
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
public class PublicController {
    @Autowired
    private AuthService authService;


    @PostMapping(value = "login")
    public ResponseEntity<BasicUserResponse> loginUser(@RequestBody LoginRequest request){
        BasicUserResponse result = authService.loginUserService(request);
        if (result != null){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping(value = "register", consumes = {"multipart/form-data"})
    public ResponseEntity<String> register(@RequestPart("request") RegisterRequest request){
        return authService.registerUserService(request);
    }
}
