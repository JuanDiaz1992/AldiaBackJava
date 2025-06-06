package com.springboot.aldiabackjava.controller;

import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.services.UserServices.AuthService;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.ChangePasswordRequest;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/users/profile")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private AuthService authService;


    @GetMapping("")
    public ResponseEntity<User> getUser() {
        User user = authService.getUserService();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/edit")
    public ResponseEntity<Map<String,Object>> editUser(@RequestBody RegisterRequest userUpdate){
        return authService.updateProfileService(userUpdate);

    }

    @PutMapping("/edit/password")
    public ResponseEntity<Map<String,String>> changePassword(@RequestBody ChangePasswordRequest request){
        return authService.changePasswordService(request);
    }

    @PutMapping("/edit/picture")
    public ResponseEntity<Map<String,String>> changeProfilePicture(@RequestBody Map<String, String> picture){
        String photoBase64  = picture.get("photo");
        return authService.changerPictureProfilService(photoBase64);
    }

    @DeleteMapping("/delete/picture")
    public ResponseEntity<String> deleteProfilePicture(){
        return authService.deleteProfilePictureService();
    }

}
