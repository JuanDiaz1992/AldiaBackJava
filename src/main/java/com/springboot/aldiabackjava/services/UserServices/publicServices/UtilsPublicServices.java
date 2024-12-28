package com.springboot.aldiabackjava.services.UserServices.publicServices;

import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.BasicUserResponse;


public class UtilsPublicServices {

    public static BasicUserResponse buildResponseLogin(User user, String token){
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


}
