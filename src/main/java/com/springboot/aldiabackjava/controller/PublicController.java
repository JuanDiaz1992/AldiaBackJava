package com.springboot.aldiabackjava.controller;


import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.LoginRequest;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.services.UserServices.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    @Autowired
    private AuthService authService;


    @PostMapping(value = "login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest request){
        String response = authService.loginUserService(request);
        if (!response.isEmpty()){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @PostMapping(value = "register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        return authService.registerUserService(request);
    }
}
