package com.springboot.aldiabackjava.services.UserServices.requestAndResponse;
import com.springboot.aldiabackjava.models.userModels.CivilStatus;
import com.springboot.aldiabackjava.models.userModels.Rol;
import com.springboot.aldiabackjava.models.userModels.TypeDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String name;
    private String lastName;
    private String surnamen;
    private Integer typeDocument;
    private String document;
    private String profilePicture;
    private String birthDate;
    private String department;
    private String town;
    private String address;
    private Integer civilStatus;
    private String numberPhone;
    private String email;
    private String occupation;
    private Boolean dataTreatment;
    private int exogenous;
    private String confirmCode;

}
