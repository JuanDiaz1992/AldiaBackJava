package com.springboot.aldiabackjava.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.aldiabackjava.repositories.IUserRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private int idUser;
    private String username;
    @JsonIgnore
    private String password;
    private int type_user;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_profile", referencedColumnName = "id_profile")
    private Profile profile;

    public User(int idUser, String username, String password, int type_user, Profile profile) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.type_user = type_user;
        this.profile = profile;
    }

    public User(String username, String password, int type_user, Profile profile) {
        this.username = username;
        this.password = password;
        this.type_user = type_user;
        this.profile = profile;
    }

    public User() {
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType_user() {
        return type_user;
    }

    public void setType_user(int type_user) {
        this.type_user = type_user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfiles(Profile profile) {
        this.profile = profile;
    }
}
