package com.springboot.aldiabackjava.services.UserServices.publicServices;

import com.springboot.aldiabackjava.repositories.userRepositories.IUserRepository;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.services.UserServices.DataValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ValidateUserAndEmail {
    @Autowired
    private DataValidate dataValidate;
    @Autowired
    private IUserRepository iUserRepository;

    public Map<String, String> validateUserAndEmailService(RegisterRequest request) {
        Map<String, String> response = new HashMap<>();
        String isPasswordOk = dataValidate.validatePassword(request.getPassword());
        String isEmailOk = dataValidate.validateEmail(request.getEmail());
        if (isPasswordOk != null ){
            response.put("message", isPasswordOk);
            response.put("status", "409");
            return response;
        }
        if(!request.getPassword().equals(request.getConfirmPassword())){
            response.put("message", "Las contraseñas no coinciden");
            response.put("status", "409");
            return response;
        }
        if (isEmailOk != null){
            response.put("message", isEmailOk);
            response.put("status", "409");
            return response;
        }
        if (iUserRepository.findByUsername(request.getUsername()).orElse(null) != null){
            response.put("message", "El nombre de usuario ya existe");
            response.put("status", "409");
            return response;
        }
        if (iUserRepository.findByEmail(request.getEmail()).orElse(null) != null){
            response.put("message", "El email ya se encuentra registrado");
            response.put("status", "409");
            return response;
        }
        if (request.getName() == null || request.getLastName() == null){
            response.put("message", "Los campos de primer nombre y primer apellido no pueden estar vacíos");
            response.put("status", "409");
            return response;
        }
        response.put("status", "200");
        response.put("message","Hemos enviado un código a tu correo, por favor verificalo");
        return response;
    }
}
