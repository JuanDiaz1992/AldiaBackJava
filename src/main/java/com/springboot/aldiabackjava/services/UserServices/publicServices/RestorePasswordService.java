package com.springboot.aldiabackjava.services.UserServices.publicServices;


import com.springboot.aldiabackjava.models.userModels.PasswordRestoreToken;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.userRepositories.IPasswordRestoreToken;
import com.springboot.aldiabackjava.repositories.userRepositories.IUserRepository;
import com.springboot.aldiabackjava.services.EmailSender;
import com.springboot.aldiabackjava.services.UserServices.DataValidate;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.ChangePasswordRequest;
import com.springboot.aldiabackjava.utils.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RestorePasswordService {
    @Autowired
    public IPasswordRestoreToken iPasswordRestoreToken;
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private DataValidate dataValidate;
    @Autowired
    private EmailSender emailSender;
    @Value("${host.front}")
    private String frontHost;



    public ResponseEntity<Map<String,String>> restorePassword(ChangePasswordRequest request){
        Map<String,String> response = new HashMap<>();
        String emailValidate = dataValidate.validateEmail(request.getEmail());
        if (emailValidate != null){
            response.put("message",emailValidate);
            response.put("status","409");
            return ResponseEntity.badRequest().body(response);
        }
        User user = iUserRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null){
            response.put("message","El correo no existe");
            response.put("status","409");
            return ResponseEntity.badRequest().body(response);
        }
        this.deletePasswordRestoreToken(user);
        String token = CodeGenerator.generateNumericCode(8);
        String linkrestore = frontHost+"reset-password/"+token;
        String subjetct = "Verifica tu Correo Electrónico";
        String message =
                        "<h1>¡Hola!</h1>" +
                        "<p>Ingresa al siguiente link para restablecer tu contraseña</p>" +
                        "<h2 style='color: #2e6c80;'>" + linkrestore + "</h2>" +
                        "<p>Si no solicitaste este cambio, ignora este correo.</p>";
        try{
            emailSender.senMail(user.getEmail(), subjetct, message);
            Boolean result = this.saveToken(user,token);
            if (result){
                response.put("message","Hemos enviado un correo con instrucciones para restablecer tu contraseña.");
                response.put("status","200");
                return ResponseEntity.ok().body(response);
            }
            response.put("message","Ah ocurrido un error, por favor intentalo más tarde.");
            response.put("status","409");
            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            response.put("message","Ah ocurrido un error, por favor intentalo más tarde");
            response.put("status","500");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public Boolean saveToken(User user,String token){
        try{
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
            PasswordRestoreToken passwordRestoreToken = PasswordRestoreToken.builder()
                    .code(token)
                    .expirationTime(expirationTime)
                    .user(user)
                    .build();
            iPasswordRestoreToken.save(passwordRestoreToken);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public void deletePasswordRestoreToken(User user){
        PasswordRestoreToken passwordRestoreToken = iPasswordRestoreToken.findByUser(user).orElse(null);
        if (passwordRestoreToken!=null){
            iPasswordRestoreToken.delete(passwordRestoreToken);
        }
    }
}