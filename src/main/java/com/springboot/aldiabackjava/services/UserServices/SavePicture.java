package com.springboot.aldiabackjava.services.UserServices;

import com.springboot.aldiabackjava.models.userModels.Profile;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.userRepositories.IProfileRepository;
import com.springboot.aldiabackjava.utils.GetDateNow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class SavePicture {
    @Autowired
    private IProfileRepository iProfileRepository;
    @Value("${user.photos.base.path}")
    private String USER_PHOTOS_BASE_PATH;
    @Value("${user.folders.base.path}")
    private String USER_FOLDERS_BASE_PATH;
    public String changerPictureProfilService(User user, byte[] decodedBytes) {
        try {
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
                log.info("Cambio de foto de usuario nuevo.");
            }
            Files.write(imagePath, decodedBytes);
            Profile profile = user.getProfile();
            String finalPath = "/private/img/users/" + user.getUsername() + "/" + filename;
            profile.setProfilePicture(finalPath);
            iProfileRepository.save(profile);
            return finalPath;
        } catch (IOException e) {
            return null;
        }
    }
}
