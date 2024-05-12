package com.springboot.aldiabackjava.models.userModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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
    @Column(name="type_user")
    private int typeUser;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_profile", referencedColumnName = "id_profile")
    private Profile profile;

    public User(int idUser, String username, String password, int typeUser, Profile profile) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.typeUser = typeUser;
        this.profile = profile;
    }

    public User(String username, String password, int typeUser, Profile profile) {
        this.username = username;
        this.password = password;
        this.typeUser = typeUser;
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

    public int getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(int type_user) {
        this.typeUser = typeUser;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfiles(Profile profile) {
        this.profile = profile;
    }
}
