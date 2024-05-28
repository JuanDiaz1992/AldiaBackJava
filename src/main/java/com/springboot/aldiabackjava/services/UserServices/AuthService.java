package com.springboot.aldiabackjava.services.UserServices;


import com.springboot.aldiabackjava.JWT.JwtTokenService;
import com.springboot.aldiabackjava.config.JwtInterceptor;
import com.springboot.aldiabackjava.models.userModels.Rol;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.BasicUserResponse;
import com.springboot.aldiabackjava.utils.DataValidate;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.LoginRequest;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.models.userModels.Profile;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.IProfileRepository;
import com.springboot.aldiabackjava.repositories.IUserRepository;
;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


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



    public BasicUserResponse loginUserService(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        User user = iUserRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtTokenService.getToken(user);
        if (!token.isEmpty()){
            BasicUserResponse basicUserResponse = new BasicUserResponse().builder()
                    .token(token)
                    .idUser(user.getIdUser())
                    .username(user.getUsername())
                    .firtsName(user.getProfile().getFirstName())
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
    public ResponseEntity<String> registerUserService(RegisterRequest request) {
        String isUsernameOk = dataValidate.validateUserName(request.getUsername());
        String isPasswordOk = dataValidate.validatePassword(request.getPassword());
        String isEmailOk = dataValidate.validateEmail(request.getEmail());
        String isDocumentOk = dataValidate.validateDocument(request.getDocument());
        if (isUsernameOk != null){
            return ResponseEntity.badRequest().body(isUsernameOk);
        }
        if (isPasswordOk != null ){
            return ResponseEntity.badRequest().body(isPasswordOk);
        }
        if (isEmailOk != null){
            return ResponseEntity.badRequest().body(isEmailOk);
        }
        if (isDocumentOk != null){
            return ResponseEntity.badRequest().body(isDocumentOk);
        }
        Profile profile = Profile.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .surnamen(request.getSurnamen())
                .typeDocument(request.getTypeDocument())
                .document(request.getDocument())
                .profilePicture("src/main/resources/static/img/sin_imagen.webp")
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
        return ResponseEntity.ok().body(token);
    }

    public User getUserService() {
        User user = jwtInterceptor.getCurrentUser();
        return user;
    }

    public User updateProfileService(RegisterRequest userUpdate){
        User user = null;
        user = jwtInterceptor.getCurrentUser();
        Profile profile = user.getProfile();
        profile.setFirstName(userUpdate.getFirstName() != null && !userUpdate.getFirstName().isEmpty() ? userUpdate.getFirstName() : profile.getFirstName());
        profile.setMiddleName(userUpdate.getMiddleName() != null && !userUpdate.getMiddleName().isEmpty() ? userUpdate.getMiddleName() : profile.getMiddleName());
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
        profile.setEmail(userUpdate.getEmail() != null && !userUpdate.getEmail().isEmpty() ? userUpdate.getEmail() : profile.getEmail());
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

    public ResponseEntity<String> changerPictureProfilService(String photo) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(photo);
            User user = jwtInterceptor.getCurrentUser();
            String directory = "src/main/resources/static/img/users/" + user.getUsername() + "/";
            Path path = Paths.get(directory);
            Files.createDirectories(path); // crea el directorio si este no existe.
            String filename = "profile.webp";
            Path imagePath = path.resolve(filename);
            String currentProfilePicturePath = user.getProfile().getProfilePicture();
            if (currentProfilePicturePath != "/img/sin_imagen.webp") {
                Path currentProfilePicture = Paths.get("src/main/resources/static"+currentProfilePicturePath);
                if (Files.exists(currentProfilePicture)) {
                    Files.delete(currentProfilePicture);
                }
            }
            Files.write(imagePath, decodedBytes);
            Profile profile = user.getProfile();
            profile.setProfilePicture("/img/users/" + user.getUsername() +"/"+ filename);
            iProfileRepository.save(profile);
            return ResponseEntity.ok().body("Cambio exitoso");
        }catch (IOException e){
            return ResponseEntity.badRequest().body("A ocurrido un error, intentelo de nuevo.");
        }

    }

    public ResponseEntity<byte[]> getUserProfilePictureService() {
        User user = jwtInterceptor.getCurrentUser();
        Path path = Paths.get("src/main/resources/static"+user.getProfile().getProfilePicture());
        try {
            Resource resource = new UrlResource(path.toUri());
            byte[] imageContent = IOUtils.toByteArray(resource.getInputStream());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageContent.length);
            return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
