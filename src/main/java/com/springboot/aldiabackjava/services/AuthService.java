package com.springboot.aldiabackjava.services;

import com.springboot.aldiabackjava.JWT.JwtTokenService;
import com.springboot.aldiabackjava.controller.userControllers.AuthResponse;
import com.springboot.aldiabackjava.controller.userControllers.LoginRequest;
import com.springboot.aldiabackjava.controller.userControllers.RegisterRequest;
import com.springboot.aldiabackjava.models.userModels.Profile;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.IProfileRepository;
import com.springboot.aldiabackjava.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IProfileRepository iProfileRepository;
    @Autowired
    private JwtTokenService jwtTokenService;


    public List<User> getAllUsers(){
        Iterable <User> result = iUserRepository.findAll();
        List <User> users = new ArrayList<>();
        result.forEach(user -> {
            users.add(user);
        });
        return users;
    }

    public User getUser(String username){
        return null;
    }

    public AuthResponse login(LoginRequest request) {
        return null;

    }

    public AuthResponse register(RegisterRequest request) {
        Profile profile = Profile.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .surnamen(request.getSurnamen())
                .typeDocument(request.getTypeDocument())
                .profilePicture(request.getProfilePicture())
                .birthDate(request.getBirthDate())
                .department(request.getDepartment())
                .town(request.getTown())
                .address(request.getAddress())
                .civilStatus(request.getCivilStatus())
                .numberPhone(request.getNumberPhone())
                .email(request.getEmail())
                .occupation(request.getOccupation())
                .dataTreatment(request.getDataTreatment())
                .exogenous(request.getExogenous()).build();
        profile = iProfileRepository.save(profile);
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(request.getRole())
                .profile(profile).build();
        iUserRepository.save(user);
        return AuthResponse.builder().token(jwtTokenService.generateToken(user)).build();
    }
}
