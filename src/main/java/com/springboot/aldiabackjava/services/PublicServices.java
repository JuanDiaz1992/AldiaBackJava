package com.springboot.aldiabackjava.services;

import com.springboot.aldiabackjava.JWT.JwtTokenService;
import com.springboot.aldiabackjava.models.userModels.Profile;
import com.springboot.aldiabackjava.models.userModels.Rol;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.userRepositories.IProfileRepository;
import com.springboot.aldiabackjava.repositories.userRepositories.IUserRepository;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.BasicUserResponse;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.LoginRequest;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.utils.DataValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class PublicServices {

    @Autowired
    private DataValidate dataValidate;
    private final IUserRepository iUserRepository;
    private final IProfileRepository iProfileRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public BasicUserResponse loginUserService(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        User user = iUserRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtTokenService.getToken(user);
        if (!token.isEmpty()){
            BasicUserResponse basicUserResponse = new BasicUserResponse().builder()
                    .token(token)
                    .idUser(user.getIdUser())
                    .username(user.getUsername())
                    .firstName(user.getProfile().getFirstName())
                    .middleName(user.getProfile().getMiddleName())
                    .lastName(user.getProfile().getLastName())
                    .surnamen(user.getProfile().getSurnamen())
                    .rol(String.valueOf(user.getRol()))
                    .photo(user.getProfile().getProfilePicture())
                    .build();
            return basicUserResponse;
        }
        return null;
    }

    public ResponseEntity<Map<String, String>> validateUserAndEmailService(RegisterRequest request) {
        Map<String, String> response = new HashMap<>();
        String isUsernameOk = dataValidate.validateUserName(request.getUsername());
        String isPasswordOk = dataValidate.validatePassword(request.getPassword());
        String isEmailOk = dataValidate.validateEmail(request.getEmail());
        if (isUsernameOk != null){
            response.put("message", isUsernameOk);
            response.put("status", "409");
            return ResponseEntity.badRequest().body(response);
        }
        if (isPasswordOk != null ){
            response.put("message", isPasswordOk);
            response.put("status", "409");
            return ResponseEntity.badRequest().body(response);
        }
        if(!request.getPassword().equals(request.getConfirmPassword())){
            response.put("message", "Las contraseñas no coinciden");
            response.put("status", "409");
            return ResponseEntity.badRequest().body(response);
        }
        if (isEmailOk != null){
            response.put("message", isEmailOk);
            response.put("status", "409");
            return ResponseEntity.badRequest().body(response);
        }
        if (iUserRepository.findByUsername(request.getUsername()).orElse(null) != null){
            response.put("message", "El nombre de usuario ya existe");
            response.put("status", "409");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        if (iProfileRepository.findByEmail(request.getEmail()).orElse(null) != null){
            response.put("message", "El email ya se encuentra registrado");
            response.put("status", "409");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        if (request.getFirstName() == null || request.getLastName() == null){
            response.put("message", "Los campos de primer nombre y primer apellido no pueden estar vacíos");
            response.put("status", "409");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        response.put("message","Bienvenido");
        response.put("status", "200");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Map<String,String>> registerUserService(RegisterRequest request) {
        Map<String, String> response = new HashMap<>();
        String isDocumentOk = dataValidate.validateDocument(request.getDocument());
        if (isDocumentOk != null){
            response.put("message","El documento es incorrecto");
            response.put("status", "409");
            return ResponseEntity.badRequest().body(response);
        }
        if (iProfileRepository.findByDocument(request.getDocument()).orElse(null) != null){
            response.put("message","El documento ya existe en el sistema");
            response.put("status", "409");
            return ResponseEntity.badRequest().body(response);
        }
        Profile profile = Profile.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .surnamen(request.getSurnamen())
                .typeDocument(request.getTypeDocument())
                .document(request.getDocument())
                .profilePicture("")
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
                .rol(Rol.USER)
                .profile(profile).build();
        iUserRepository.save(user);
        String token = jwtTokenService.getToken(user);
        response.put("token", token);
        response.put("idUser", Integer.toString(user.getIdUser()));
        response.put("username", user.getUsername());
        response.put("firstName", user.getProfile().getFirstName());
        response.put("middleName", user.getProfile().getMiddleName());
        response.put("lastName", user.getProfile().getLastName());
        response.put("surname", user.getProfile().getSurnamen());
        response.put("rol", String.valueOf(user.getRol()));
        response.put("photo", user.getProfile().getProfilePicture());
        response.put("status", "200");
        response.put("message","Bienvenido "+user.getProfile().getFirstName()+", ahora puedes iniciar sesión");
        return ResponseEntity.ok().body(response);
    }

}
