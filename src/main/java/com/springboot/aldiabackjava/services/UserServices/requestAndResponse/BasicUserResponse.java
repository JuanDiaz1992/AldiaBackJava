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
    private String name;
    private String lastName;
    private String surnamen;
    private String rol;
    private String photo;
    private String occupation;
}
