package com.springboot.aldiabackjava.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DataValidate {
    @Autowired
    private final PasswordEncoder passwordEncoder;


    public String validatePassword(String password){
        if (password == null) {
            return "La contraseña no puede estar vacía";
        }
        if (password.length() < 8){
            return "La nueva contraseña debe tener más de 8 caracteres";
        }
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).+$";
        if (!password.matches(regex)){
            return "La nueva contraseña debe contener letras y números";
        }
        return null;
    }

    public String validateUserName(String userName){
        if (userName.isEmpty()){
            return "El usuario no puede estar vacío";
        }
        if (userName.length() < 5){
            return "El usuario debe contener más de 5 caracteres";
        }
        if (userName.length()>10){
            return "El usuario no debe contener más de 10 caracteres";
        }
        return null;
    }

    public String validateEmail(String email) {
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || email.isEmpty()) {
            return "El correo no puede estar vacío";
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return "El formato del correo no es válido";
        }

        return null;
    }

    public String validateDocument(String document){
        String DOCUMENT_REGEX = "^[0-9]+$";
        if (document == null || document.isEmpty()){
            return "El documento no puede estar vacío";
        }
        if (!document.matches(DOCUMENT_REGEX)){
            return "El documento no puede contener letras";
        }
        if (document.length()<7){
            return "El documento debe contener más de 7 dígitos";
        }
        if (document.length()>15){
            return "El documento no debe contener más de 15 dígitos";
        }
        return null;

    }

}
