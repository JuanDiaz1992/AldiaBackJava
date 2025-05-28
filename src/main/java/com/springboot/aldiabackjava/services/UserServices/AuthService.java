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

    public ResponseEntity<Map<String, Object>> updateProfileService(RegisterRequest userUpdate) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = jwtInterceptor.getCurrentUser();
            if (user == null || user.getProfile() == null) {
                response.put("status", 401);
                response.put("message", "Usuario no autorizado o perfil no encontrado");
                return ResponseEntity.status(401).body(response);
            }

            Profile profile = user.getProfile();

            // Actualizar campos si no vienen vacíos o nulos
            if (userUpdate.getName() != null && !userUpdate.getName().isBlank())
                profile.setName(userUpdate.getName());

            if (userUpdate.getLastName() != null && !userUpdate.getLastName().isBlank())
                profile.setLastName(userUpdate.getLastName());

            if (userUpdate.getSurnamen() != null && !userUpdate.getSurnamen().isBlank())
                profile.setSurnamen(userUpdate.getSurnamen());



            if (userUpdate.getDocument() != null && !userUpdate.getDocument().isBlank())
                profile.setDocument(userUpdate.getDocument());

            if (userUpdate.getBirthDate() != null && !userUpdate.getBirthDate().isBlank())
                profile.setBirthDate(userUpdate.getBirthDate());

            if (userUpdate.getDepartment() != null && !userUpdate.getDepartment().isBlank())
                profile.setDepartment(userUpdate.getDepartment());

            if (userUpdate.getTown() != null && !userUpdate.getTown().isBlank())
                profile.setTown(userUpdate.getTown());

            if (userUpdate.getAddress() != null && !userUpdate.getAddress().isBlank())
                profile.setAddress(userUpdate.getAddress());


            if (userUpdate.getNumberPhone() != null && !userUpdate.getNumberPhone().isBlank())
                profile.setNumberPhone(userUpdate.getNumberPhone());

            if (userUpdate.getOccupation() != null && !userUpdate.getOccupation().isBlank())
                profile.setOccupation(userUpdate.getOccupation());

            if (userUpdate.getDataTreatment() != null)
                profile.setDataTreatment(userUpdate.getDataTreatment());

            iProfileRepository.save(profile);

            response.put("status", 200);
            response.put("message", "Sus datos se modificaron correctamente");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al actualizar el perfil del usuario", e);
            response.put("status", 400);
            response.put("message", "Ocurrió un error al actualizar sus datos. Intente más tarde.");
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
