package com.springboot.aldiabackjava.controller;


import com.springboot.aldiabackjava.services.UserServices.publicServices.GoogleService;
import com.springboot.aldiabackjava.services.UserServices.publicServices.LoginService;
import com.springboot.aldiabackjava.services.UserServices.publicServices.RegisterService;
import com.springboot.aldiabackjava.services.UserServices.publicServices.RestorePasswordService;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class PublicController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private GoogleService googleService;
    @Autowired
    private RestorePasswordService restorePasswordService;


    @PostMapping("/login")
    public ResponseEntity<BasicUserResponse> loginUser(@RequestBody LoginRequest request){
        BasicUserResponse result = loginService.loginUserService(request);
        if (result != null){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody RegisterRequest request){
        return registerService.registerUserService(request);
    }

    @PostMapping("/emailvalidate")
    public ResponseEntity<Map<String,String>> validateEmmail(@RequestBody RegisterRequest request){
        return registerService.validateMail(request);
    }

    @PostMapping("/google")
    public ResponseEntity<BasicUserResponse> loginUserWhitGoogle(@RequestBody Map<String,Object> request){
        BasicUserResponse result =  googleService.loginGoogleService(request);
        if (result != null){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/restorepassword")
    public ResponseEntity<Map<String,String>> restorePassword(@RequestBody ResetPasswordRequest request){
        return restorePasswordService.restorePassword(request);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<Map<String,String>> resetPassword(@RequestBody ChangePasswordRequest request){
        return restorePasswordService.changePassword(request);
    }
}
