package com.springboot.aldiabackjava.services.UserServices;


import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.ChangePasswordRequest;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.models.userModels.Profile;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.userRepositories.IProfileRepository;
import com.springboot.aldiabackjava.repositories.userRepositories.IUserRepository;
import com.springboot.aldiabackjava.utils.GetDateNow;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;



@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Autowired
    private DataValidate dataValidate;
    @Autowired
    private final IUserRepository iUserRepository;
    @Autowired
    private final IProfileRepository iProfileRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private SavePicture savePicture;
    @Value("${user.photos.base.path}")
    private String USER_PHOTOS_BASE_PATH;
    @Value("${user.folders.base.path}")
    private String USER_FOLDERS_BASE_PATH;

    public User getUserService() {
        User user = jwtInterceptor.getCurrentUser();
        return user;
    }

    public ResponseEntity<Map<String,String>> updateProfileService(RegisterRequest userUpdate){
        Map<String,String> response = new HashMap<>();
        User user = jwtInterceptor.getCurrentUser();
        Profile profile = user.getProfile();
        try{
            profile.setName(userUpdate.getName() != null && !userUpdate.getName().isEmpty() ? userUpdate.getName() : profile.getName());
            profile.setLastName(userUpdate.getLastName() != null && !userUpdate.getLastName().isEmpty() ? userUpdate.getLastName() : profile.getLastName());
            profile.setSurnamen(userUpdate.getSurnamen() != null && !userUpdate.getSurnamen().isEmpty() ? userUpdate.getSurnamen() : profile.getSurnamen());
            profile.setTypeDocument(userUpdate.getTypeDocument() != null ? userUpdate.getTypeDocument() : profile.getTypeDocument());
            profile.setDocument(userUpdate.getDocument() != null && !userUpdate.getDocument().isEmpty() ? userUpdate.getDocument() : profile.getDocument());
            profile.setBirthDate(userUpdate.getBirthDate() != null && !userUpdate.getBirthDate().isEmpty() ? userUpdate.getBirthDate() : profile.getBirthDate());
            profile.setDepartment(userUpdate.getDepartment() != null && !userUpdate.getDepartment().isEmpty() ? userUpdate.getDepartment() : profile.getDepartment());
            profile.setTown(userUpdate.getTown() != null && !userUpdate.getTown().isEmpty() ? userUpdate.getTown() : profile.getTown());
            profile.setAddress(userUpdate.getAddress() != null && !userUpdate.getAddress().isEmpty() ? userUpdate.getAddress() : profile.getAddress());
            profile.setCivilStatus(userUpdate.getCivilStatus() != null? userUpdate.getCivilStatus() : profile.getCivilStatus());
            profile.setNumberPhone(userUpdate.getNumberPhone() != null && !userUpdate.getNumberPhone().isEmpty() ? userUpdate.getNumberPhone() : profile.getNumberPhone());
            profile.setOccupation(userUpdate.getOccupation() != null && !userUpdate.getOccupation().isEmpty() ? userUpdate.getOccupation() : profile.getOccupation());
            iProfileRepository.save(profile);
            log.info(userUpdate.toString());
            return ResponseEntity.ok().body(response);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<Map<String,String>> changePasswordService(ChangePasswordRequest request){
        User user = jwtInterceptor.getCurrentUser();
        Map<String,String> response = new HashMap<>();
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())){
            response.put("status","400");
            response.put("message", "Te registraste utilizando un servicio externo (Google, Facebook o GitHub). No es posible cambiar la contraseña.");
            return ResponseEntity.badRequest().body(response);
        }
        String morValidations = dataValidate.validatePassword(request.getNewPassword());
        if (morValidations != null){
            response.put("status","409");
            response.put("message",morValidations);
            return ResponseEntity.badRequest().body(response);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        iUserRepository.save(user);
        response.put("status","200");
        response.put("message","La contraseña se cambió correctamente");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Map<String, String>> changerPictureProfilService(String photo) {
        Map response =  new HashMap<>();
        byte[] decodedBytes = Base64.getDecoder().decode(photo);
        User user = jwtInterceptor.getCurrentUser();
        String finalPath = savePicture.changerPictureProfilService(user,decodedBytes);
        if (finalPath==null){
            response.put("message", "A ocurrido un error, intentelo de nuevo.");
            response.put("status", "400");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        response.put("message", "Cambio exitoso");
        response.put("status", "200");
        response.put("url",finalPath);
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<String> deleteProfilePictureService() {
        User user = jwtInterceptor.getCurrentUser();
        if (user.getProfile().getProfilePicture().equals("/img/sin_imagen.webp")){
            return ResponseEntity.ok().body("No se puede relizar esta acción");
        }
        try {
            Files.delete(Path.of("src/main/resources/static"+user.getProfile().getProfilePicture()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.getProfile().setProfilePicture("/img/sin_imagen.webp");
        iProfileRepository.save(user.getProfile());
        return ResponseEntity.ok().body("Foto eliminada");

    }


}
