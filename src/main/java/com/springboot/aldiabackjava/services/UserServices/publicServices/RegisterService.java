package com.springboot.aldiabackjava.services.UserServices.publicServices;

import com.springboot.aldiabackjava.models.userModels.EmailVerification;
import com.springboot.aldiabackjava.models.userModels.Profile;
import com.springboot.aldiabackjava.models.userModels.Rol;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.userRepositories.IEmailVerification;
import com.springboot.aldiabackjava.repositories.userRepositories.IProfileRepository;
import com.springboot.aldiabackjava.repositories.userRepositories.IUserRepository;
import com.springboot.aldiabackjava.services.EmailSender;
import com.springboot.aldiabackjava.services.UserServices.DataValidate;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.RegisterRequest;
import com.springboot.aldiabackjava.utils.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegisterService {
    @Autowired
    private IProfileRepository iProfileRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IEmailVerification iEmailVerification;
    @Autowired
    private ValidateUserAndEmail validateUserAndEmail;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private DataValidate dataValidate;

    public ResponseEntity<Map<String,String>> registerUserService(RegisterRequest request) {
        Map <String,String> result = new HashMap<>();
        String mailValidate = dataValidate.validateEmail(request.getEmail());
        if (mailValidate!=null){
            result.put("status","409");
            result.put("message",mailValidate);
            return ResponseEntity.badRequest().body(result);
        }
        Boolean isCodeValidate = this.validateCode(request.getEmail(),request.getConfirmCode());
        try{
            if (isCodeValidate){
                Profile profile = Profile.builder()
                        .name(request.getName())
                        .lastName(request.getLastName())
                        .surnamen(request.getSurnamen())
                        .build();
                profile = iProfileRepository.save(profile);
                User user = User.builder()
                        .password(passwordEncoder.encode( request.getPassword()))
                        .rol(Rol.USER)
                        .email(request.getEmail())
                        .isFromExternalApp(false)
                        .profile(profile).build();
                user = iUserRepository.save(user);
                String email = user.getEmail();
                String username = email.substring(0, email.indexOf('@'))+"."+user.getIdUser();
                user.setUsername(username);
                iUserRepository.save(user);
                EmailVerification emailVerification = iEmailVerification.findByEmail(email).orElse(null);
                if (emailVerification!=null){
                    iEmailVerification.delete(emailVerification);
                }
                result.put("status", "200");
                result.put("message","Bienvenido, ahora puedes iniciar sesión");
                return ResponseEntity.ok().body(result);
            }else{
                result.put("status","409");
                result.put("message","El código ingresado no es valido");
                return ResponseEntity.badRequest().body(result);
            }

        }catch (Exception e){
            result.put("status","409");
            result.put("message","Ah ocurrido un error, intentalo de nuevo más tarde");
            return ResponseEntity.badRequest().body(result);
        }

    }

    public boolean validateCode(String email, String code) {
        EmailVerification verification = iEmailVerification.findByEmail(email).orElse(null);
        if (verification != null && verification.getCode().equals(code)) {
            if (LocalDateTime.now().isBefore(verification.getExpirationTime())) {
                return true;
            }
        }
        return false;
    }

    public ResponseEntity<Map<String,String>> validateMail(RegisterRequest request){
        Map <String,String> result = validateUserAndEmail.validateUserAndEmailService(request);
        if (result.get("status").equals("409")){
            return ResponseEntity.badRequest().body(result);
        }else{
            EmailVerification emailVerification = iEmailVerification.findByEmail(request.getEmail()).orElse(null);
            if (emailVerification!=null){
                iEmailVerification.delete(emailVerification);
            }
            String code = CodeGenerator.generateNumericCode(6);
            String subjetct = "Verifica tu Correo Electrónico";
            String message =
                    "<h1>¡Hola!</h1>" +
                    "<p>Gracias por registrarte. Por favor, usa el siguiente código para verificar tu correo electrónico:</p>" +
                    "<h2 style='color: #2e6c80;'>" + code + "</h2>" +
                    "<p>Si no solicitaste este código, ignora este correo.</p>";
            emailSender.senMail(request.getEmail(),subjetct, message);
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
            EmailVerification emailVerification1 = EmailVerification.builder()
                    .email(request.getEmail())
                    .code(code)
                    .expirationTime(expirationTime)
                    .build();
            iEmailVerification.save(emailVerification1);
            return ResponseEntity.ok().body(result);
        }
    }
}
