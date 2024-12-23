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
import com.springboot.aldiabackjava.utils.GoogleTokenValidator;
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
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return null;
        }
        User user = iUserRepository.findByEmail(request.getEmail()).orElseThrow();
        if (user.isFromExternalApp()){
            return null;
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),request.getPassword()));
        String token = jwtTokenService.getToken(user);
        return this.buildResponseLogin(user,token);
    }

    public ResponseEntity<Map<String,String>> registerUserService(RegisterRequest request) {
        Map <String,String> result = this.validateUserAndEmailService(request);
        if (result.get("status").equals("409")){
            return ResponseEntity.badRequest().body(result);
        }
        try{
            Profile profile = Profile.builder()
                    .name(request.getName())
                    .lastName(request.getLastName())
                    .surnamen(request.getSurnamen())
                    .build();
            profile = iProfileRepository.save(profile);
            User user = User.builder()
                    .password(passwordEncoder.encode( request.getPassword()))
                    .rol(Rol.USER)
                    .email(request.getEmail())
                    .isFromExternalApp(false)
                    .profile(profile).build();
            user = iUserRepository.save(user);
            String email = user.getEmail();
            String username = email.substring(0, email.indexOf('@'))+"."+user.getIdUser();
            user.setUsername(username);
            iUserRepository.save(user);
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            result.put("status","409");
            result.put("message","Ah ocurrido un error, intentalo de nuevo más tarde");
            return ResponseEntity.badRequest().body(result);
        }

    }

    public BasicUserResponse loginGoogleService(Map<String,Object> request) {
        Map<String,Object>dataUser = (Map<String, Object>) request.get("response");
        Map<String,Object>profile = (Map<String, Object>) dataUser.get("profileObj");
        String idToken = (String) dataUser.get("tokenId");
        boolean isValidToken = GoogleTokenValidator.verifyGoogleToken(idToken);
        try {
            if (isValidToken){
                String email = (String) profile.get("email");
                User user = iUserRepository.findByEmail(email).orElse(null);
                String token = "";
                if (user!=null){
                    token = jwtTokenService.getToken(user);
                }else{
                    user = this.saveUserByGoogle(profile);
                    token = jwtTokenService.getToken(user);
                }
                return this.buildResponseLogin(user,token);
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }

    public static BasicUserResponse buildResponseLogin(User user,String token){
        if (!token.isEmpty()){
            BasicUserResponse basicUserResponse = new BasicUserResponse().builder()
                    .token(token)
                    .idUser(user.getIdUser())
                    .name(user.getProfile().getName())
                    .lastName(user.getProfile().getLastName())
                    .surnamen(user.getProfile().getSurnamen())
                    .rol(String.valueOf(user.getRol()))
                    .occupation(user.getProfile().getOccupation())
                    .photo(user.getProfile().getProfilePicture())
                    .isFromExternalApp(user.isFromExternalApp())
                    .build();
            return basicUserResponse;
        }
        return null;
    }

    public Map<String, String> validateUserAndEmailService(RegisterRequest request) {
        Map<String, String> response = new HashMap<>();
        String isPasswordOk = dataValidate.validatePassword(request.getPassword());
        String isEmailOk = dataValidate.validateEmail(request.getEmail());
        if (isPasswordOk != null ){
            response.put("message", isPasswordOk);
            response.put("status", "409");
            return response;
        }
        if(!request.getPassword().equals(request.getConfirmPassword())){
            response.put("message", "Las contraseñas no coinciden");
            response.put("status", "409");
            return response;
        }
        if (isEmailOk != null){
            response.put("message", isEmailOk);
            response.put("status", "409");
            return response;
        }
        if (iUserRepository.findByUsername(request.getUsername()).orElse(null) != null){
            response.put("message", "El nombre de usuario ya existe");
            response.put("status", "409");
            return response;
        }
        if (iUserRepository.findByEmail(request.getEmail()).orElse(null) != null){
            response.put("message", "El email ya se encuentra registrado");
            response.put("status", "409");
            return response;
        }
        if (request.getName() == null || request.getLastName() == null){
            response.put("message", "Los campos de primer nombre y primer apellido no pueden estar vacíos");
            response.put("status", "409");
            return response;
        }
        response.put("status", "200");
        response.put("message","Bienvenido, ahora puedes iniciar sesión");
        return response;
    }

    public User saveUserByGoogle(Map<String,Object> profile){
        try{
            String fullName = (String) profile.get("familyName");
            String[] fullNameParts = fullName.split(" ");
            String imageUrl = (String) profile.get("imageUrl");
            boolean hasCustomImage = imageUrl != null && !imageUrl.contains("default-user");
            Profile profileToUpdate = Profile.builder()
                    .name(profile.get("givenName").toString())
                    .lastName(fullNameParts[0])
                    .surnamen(fullNameParts.length > 1?fullNameParts[1]:"")
                    .profilePicture(hasCustomImage ? imageUrl :"")
                    .build();
            profileToUpdate = iProfileRepository.save(profileToUpdate);
            User user = User.builder()
                    .rol(Rol.USER)
                    .email(profile.get("email").toString())
                    .isFromExternalApp(true)
                    .profile(profileToUpdate).build();
            user = iUserRepository.save(user);
            String email = user.getEmail();
            String username = email.substring(0, email.indexOf('@'))+"."+user.getIdUser();
            user.setUsername(username);
            iUserRepository.save(user);
            return user;
        }catch (Exception e){
            return null;
        }


    }
}
