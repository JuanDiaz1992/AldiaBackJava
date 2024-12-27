package com.springboot.aldiabackjava.repositories.userRepositories;

import com.springboot.aldiabackjava.models.userModels.EmailVerification;
import com.springboot.aldiabackjava.models.userModels.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEmailVerification extends JpaRepository<EmailVerification,String> {
    Optional<EmailVerification> findByEmail(String email);
}
