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
@Table(name = "passwordrestoretoken")
public class PasswordRestoreToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_restorepassword")
    private int idRestorePassword;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime expirationTime;
}
