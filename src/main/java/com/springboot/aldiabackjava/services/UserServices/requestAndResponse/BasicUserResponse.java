package com.springboot.aldiabackjava.services.UserServices.requestAndResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicUserResponse {
    private String token;
    private int idUser;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String surnamen;
    private String rol;
    private String photo;
}
