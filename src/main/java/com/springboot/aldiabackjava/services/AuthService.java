package com.springboot.aldiabackjava.services;

import com.springboot.aldiabackjava.JWT.JwtTokenService;
import com.springboot.aldiabackjava.models.userModels.Role;
import com.springboot.aldiabackjava.services.requestAndResponse.AuthResponse;
import com.springboot.aldiabackjava.services.requestAndResponse.LoginRequest;
import com.springboot.aldiabackjava.services.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.models.userModels.Profile;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.IProfileRepository;
import com.springboot.aldiabackjava.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepository iUserRepository;
    private final IProfileRepository iProfileRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        UserDetails user = iUserRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtTokenService.getToken(user);
        return AuthResponse.builder().token(token).build();

    }

    public AuthResponse register(RegisterRequest request) {
        Profile profile = Profile.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .surnamen(request.getSurnamen())
                .typeDocument(request.getTypeDocument())
                .document(request.getDocument())
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
                .password(passwordEncoder.encode( request.getPassword()))
                .role(Role.USER)
                .profile(profile).build();
        iUserRepository.save(user);
        return AuthResponse.builder().token(jwtTokenService.getToken(user)).build();
    }
}
