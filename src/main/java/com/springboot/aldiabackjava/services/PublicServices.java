package com.springboot.aldiabackjava.services;

import com.springboot.aldiabackjava.JWT.JwtTokenService;
import com.springboot.aldiabackjava.config.GoogleTokenValidator;
import com.springboot.aldiabackjava.models.userModels.EmailVerification;
import com.springboot.aldiabackjava.models.userModels.Profile;
import com.springboot.aldiabackjava.models.userModels.Rol;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.userRepositories.IEmailVerification;
import com.springboot.aldiabackjava.repositories.userRepositories.IProfileRepository;
import com.springboot.aldiabackjava.repositories.userRepositories.IUserRepository;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.BasicUserResponse;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.LoginRequest;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.utils.CodeGenerator;
import com.springboot.aldiabackjava.utils.DataValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class PublicServices {
    private final EmailSender emailSender;

    @Autowired
    private DataValidate dataValidate;
    private final IUserRepository iUserRepository;
    private final IProfileRepository iProfileRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IEmailVerification iEmailVerification;

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

    public ResponseEntity<Map<String,String>> validateMail(RegisterRequest request){
        Map <String,String> result = this.validateUserAndEmailService(request);
        if (result.get("status").equals("409")){
            return ResponseEntity.badRequest().body(result);
        }else{
            EmailVerification emailVerification = iEmailVerification.findByEmail(request.getEmail()).orElse(null);
            if (emailVerification!=null){
                iEmailVerification.delete(emailVerification);
            }
            String code = CodeGenerator.generateNumericCode(6);
            emailSender.senMail(request.getEmail(),code);
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
            EmailVerification emailVerification1 = EmailVerification.builder()
                    .email(request.getEmail())
                    .code(code)
                    .expirationTime(expirationTime)
                    .build();
            iEmailVerification.save(emailVerification1);
            return ResponseEntity.ok().body(result);
        }
    }

    public ResponseEntity<Map<String,String>> registerUserService(RegisterRequest request) {
        Map <String,String> result = new HashMap<>();
        Boolean isCodeValidate = validateCode(request.getEmail(),request.getConfirmCode());
        try{
            if (isCodeValidate){
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
                EmailVerification emailVerification = iEmailVerification.findByEmail(email).orElse(null);
                if (emailVerification!=null){
                    iEmailVerification.delete(emailVerification);
                }
                result.put("status", "200");
                result.put("message","Bienvenido, ahora puedes iniciar sesión");
                return ResponseEntity.ok().body(result);
            }else{

                result.put("status","409");
                result.put("message","El código ingresado no es valido");
                return ResponseEntity.badRequest().body(result);
            }

        }catch (Exception e){
            result.put("status","409");
            result.put("message","Ah ocurrido un error, intentalo de nuevo más tarde");
            return ResponseEntity.badRequest().body(result);
        }

    }

    public BasicUserResponse loginGoogleService(Map<String,Object> request) {
        log.info(request.toString());
        Map<String,Object>dataUser = (Map<String, Object>) request.get("response");
        String idToken = (String) dataUser.get("credential");
        try {
            Map<String, Object> userData = GoogleTokenValidator.getUserDataFromToken(idToken);
            log.info(userData.toString());
            if (!userData.isEmpty()){
                String email = (String) userData.get("email");
                User user = iUserRepository.findByEmail(email).orElse(null);
                String token = "";
                if (user!=null){
                    token = jwtTokenService.getToken(user);
                }else{
                    user = this.saveUserByGoogle(userData);
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

    public User saveUserByGoogle(Map<String,Object> profile){
        try{
            String fullName = (String) profile.get("familyName");
            String[] fullNameParts = fullName.split(" ");
            String imageUrl = (String) profile.get("picture");
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
        response.put("message","Hemos enviado un código a tu correo, por favor verificalo");
        return response;
    }

    public boolean validateCode(String email, String code) {
        EmailVerification verification = iEmailVerification.findByEmail(email).orElse(null);
        if (verification != null && verification.getCode().equals(code)) {
            if (LocalDateTime.now().isBefore(verification.getExpirationTime())) {
                return true;
            }
        }
        return false;
    }

}
