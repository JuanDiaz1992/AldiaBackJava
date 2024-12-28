package com.springboot.aldiabackjava.services.UserServices.publicServices;
import com.springboot.aldiabackjava.JWT.JwtTokenService;
import com.springboot.aldiabackjava.models.userModels.User;
import com.springboot.aldiabackjava.repositories.userRepositories.IUserRepository;
import com.springboot.aldiabackjava.services.UserServices.DataValidate;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.BasicUserResponse;
import com.springboot.aldiabackjava.services.UserServices.requestAndResponse.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtTokenService jwtTokenService;
    @Autowired
    private DataValidate dataValidate;
    public BasicUserResponse loginUserService(LoginRequest request) {
        String result = dataValidate.validateEmail(request.getEmail());
        if (result != null) {
            return null;
        }
        User user = iUserRepository.findByEmail(request.getEmail()).orElseThrow();
        if (user.isFromExternalApp()){
            return null;
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),request.getPassword()));
        String token = jwtTokenService.getToken(user);
        return UtilsPublicServices.buildResponseLogin(user,token);
    }


}
