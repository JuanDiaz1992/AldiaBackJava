package com.springboot.aldiabackjava.models.userModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private int idUser;
    private String username;
    @JsonIgnore
    private String password;
    @Column(name="role")
    private Role role;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_profile", referencedColumnName = "id_profile")
    private Profile profile;

    public User() {

    }

    public User(String username, String password, Role role, Profile profile) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.profile = profile;
    }

    public User(int idUser, String username, String password, Role role, Profile profile) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.role = role;
        this.profile = profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
