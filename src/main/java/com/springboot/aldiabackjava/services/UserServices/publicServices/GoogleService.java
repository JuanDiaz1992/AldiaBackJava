package com.springboot.aldiabackjava.services.UserServices.publicServices;

import com.springboot.aldiabackjava.JWT.JwtTokenService;
import com.springboot.aldiabackjava.config.GoogleTokenValidator;
import com.springboot.aldiabackjava.models.userModels.Profile;
import com.springboot.aldiabackjava.models.userModels.Rol;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.userRepositories.IProfileRepository;
import com.springboot.aldiabackjava.repositories.userRepositories.IUserRepository;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.BasicUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleService {
    @Autowired
    private final IUserRepository iUserRepository;
    @Autowired
    private final IProfileRepository iProfileRepository;
    @Autowired
    private final JwtTokenService jwtTokenService;


    public BasicUserResponse loginGoogleService(Map<String,Object> request) {
        Map<String,Object>dataUser = (Map<String, Object>) request.get("response");
        String idToken = (String) dataUser.get("credential");
        try {
            Map<String, Object> userData = GoogleTokenValidator.getUserDataFromToken(idToken);
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
                return UtilsPublicServices.buildResponseLogin(user,token);
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
}
