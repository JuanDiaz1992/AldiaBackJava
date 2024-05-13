package com.springboot.aldiabackjava.controller.userControllers;


import com.springboot.aldiabackjava.services.requestAndResponse.AuthResponse;
import com.springboot.aldiabackjava.services.requestAndResponse.LoginRequest;
import com.springboot.aldiabackjava.services.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private AuthService authService;


    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }
}
