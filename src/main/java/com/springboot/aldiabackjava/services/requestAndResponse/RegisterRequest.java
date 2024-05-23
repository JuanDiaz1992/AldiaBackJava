package com.springboot.aldiabackjava.services.requestAndResponse;
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
    private int idUser;
    private String username;
    private String password;
    private Rol rol;
    private int idPRofile;
    private String firstName;
    private String middleName;
    private String lastName;
    private String surnamen;
    private TypeDocument typeDocument;
    private String document;
    private String profilePicture;
    private String birthDate;
    private String department;
    private String town;
    private String address;
    private CivilStatus civilStatus;
    private String numberPhone;
    private String email;
    private String occupation;
    private Boolean dataTreatment;
    private int exogenous;
}
