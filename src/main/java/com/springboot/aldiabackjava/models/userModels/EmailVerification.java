package com.springboot.aldiabackjava.models.userModels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "emailVerification")
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_email_verification")
    private int idEmailVerification;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private LocalDateTime expirationTime;

}
