package com.springboot.aldiabackjava.services.UserServices;


import com.springboot.aldiabackjava.JWT.JwtTokenService;
import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.utils.DataValidate;
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
import org.springframework.security.authentication.AuthenticationManager;
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

    private final IUserRepository iUserRepository;
    private final IProfileRepository iProfileRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${user.photos.base.path}")
    private String USER_PHOTOS_BASE_PATH;

    @Value("${user.folders.base.path}")
    private String USER_FOLDERS_BASE_PATH;

    public User getUserService() {
        User user = jwtInterceptor.getCurrentUser();
        return user;
    }

    public User updateProfileService(RegisterRequest userUpdate){
        User user = null;
        user = jwtInterceptor.getCurrentUser();
        Profile profile = user.getProfile();
        profile.setName(userUpdate.getName() != null && !userUpdate.getName().isEmpty() ? userUpdate.getName() : profile.getName());
        profile.setLastName(userUpdate.getLastName() != null && !userUpdate.getLastName().isEmpty() ? userUpdate.getLastName() : profile.getLastName());
        profile.setSurnamen(userUpdate.getSurnamen() != null && !userUpdate.getSurnamen().isEmpty() ? userUpdate.getSurnamen() : profile.getSurnamen());
        profile.setTypeDocument(userUpdate.getTypeDocument() != null ? userUpdate.getTypeDocument() : profile.getTypeDocument());
        profile.setDocument(userUpdate.getDocument() != null && !userUpdate.getDocument().isEmpty() ? userUpdate.getDocument() : profile.getDocument());
        profile.setProfilePicture(userUpdate.getProfilePicture() != null && !userUpdate.getProfilePicture().isEmpty() ? userUpdate.getProfilePicture() : profile.getProfilePicture());
        profile.setBirthDate(userUpdate.getBirthDate() != null && !userUpdate.getBirthDate().isEmpty() ? userUpdate.getBirthDate() : profile.getBirthDate());
        profile.setDepartment(userUpdate.getDepartment() != null && !userUpdate.getDepartment().isEmpty() ? userUpdate.getDepartment() : profile.getDepartment());
        profile.setTown(userUpdate.getTown() != null && !userUpdate.getTown().isEmpty() ? userUpdate.getTown() : profile.getTown());
        profile.setAddress(userUpdate.getAddress() != null && !userUpdate.getAddress().isEmpty() ? userUpdate.getAddress() : profile.getAddress());
        profile.setCivilStatus(userUpdate.getCivilStatus() != null? userUpdate.getCivilStatus() : profile.getCivilStatus());
        profile.setNumberPhone(userUpdate.getNumberPhone() != null && !userUpdate.getNumberPhone().isEmpty() ? userUpdate.getNumberPhone() : profile.getNumberPhone());
        profile.setOccupation(userUpdate.getOccupation() != null && !userUpdate.getOccupation().isEmpty() ? userUpdate.getOccupation() : profile.getOccupation());
        iProfileRepository.save(profile);
        return user;
    }

    public ResponseEntity<String> changePasswordService(String newPassword){
        User user = jwtInterceptor.getCurrentUser();
        if (passwordEncoder.matches(newPassword, user.getPassword())){
            return ResponseEntity.badRequest().body("La nueva contraseña no puede ser igual a la anterior");
        }
        String morValidations = dataValidate.validatePassword(newPassword);
        if (morValidations != null){
            return ResponseEntity.badRequest().body(morValidations);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        iUserRepository.save(user);
        return ResponseEntity.ok().body("La contraseña se cambió correctamente");

    }

    public ResponseEntity<Map<String, String>> changerPictureProfilService(String photo) {
        Map response =  new HashMap<>();
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(photo);
            User user = jwtInterceptor.getCurrentUser();
            String directory = this.USER_PHOTOS_BASE_PATH + user.getUsername() + "/";
            Path path = Paths.get(directory);
            Files.createDirectories(path); // crea el directorio si este no existe.

            String filename = GetDateNow.getCode() + "profile.webp";
            Path imagePath = path.resolve(filename);
            String currentProfilePicturePath = user.getProfile().getProfilePicture();

            try {
                if (!"private/img/sin_imagen.webp".equals(currentProfilePicturePath)) {
                    Path currentProfilePicture = Paths.get(this.USER_FOLDERS_BASE_PATH + currentProfilePicturePath);
                    if (Files.exists(currentProfilePicture)) {
                        Files.delete(currentProfilePicture);
                    }
                }
            }catch (Exception e){
                log.error("Cambio de foto de usuario nuevo.");
            }
            Files.write(imagePath, decodedBytes);
            Profile profile = user.getProfile();
            String finalPath = "/private/img/users/" + user.getUsername() + "/" + filename;

            profile.setProfilePicture(finalPath);
            iProfileRepository.save(profile);
            response.put("message", "Cambio exitoso");
            response.put("status", "200");
            response.put("url",finalPath);
            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            response.put("message", "A ocurrido un error, intentelo de nuevo.");
            response.put("status", "400");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
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
