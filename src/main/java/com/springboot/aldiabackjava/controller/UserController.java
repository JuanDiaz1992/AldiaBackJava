package com.springboot.aldiabackjava.controller;

import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.services.UserServices.AuthService;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.ChangePasswordRequest;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private AuthService authService;


    @GetMapping("/profile")
    public ResponseEntity<User> getUser() {
        User user = authService.getUserService();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/edit")
    public User editUser(@RequestBody RegisterRequest userUpdate){
        User user = authService.updateProfileService(userUpdate);
        return user;
    }

    @PutMapping("/edit/password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request){
        return authService.changePasswordService(request.getNewPassword());
    }

    @PutMapping("/edit/picture")
    public ResponseEntity<String> changeProfilePicture(@RequestParam("file") MultipartFile file){
        return authService.changerPictureProfilService(file);
    }

    @GetMapping("/profile/picture")
    public ResponseEntity<byte[]> getProfilePicture(){
        return authService.getUserProfilePictureService();
    }

}
