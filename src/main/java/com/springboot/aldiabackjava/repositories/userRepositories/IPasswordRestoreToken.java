package com.springboot.aldiabackjava.repositories.userRepositories;

import com.springboot.aldiabackjava.models.userModels.EmailVerification;
import com.springboot.aldiabackjava.models.userModels.PasswordRestoreToken;
import com.springboot.aldiabackjava.models.userModels.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPasswordRestoreToken  extends JpaRepository<PasswordRestoreToken,String> {
    Optional<PasswordRestoreToken> findByUser(User user);
}
