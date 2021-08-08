package com.example.oauth2_login.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Column
    private Role role;

    @Column(nullable = false)
    private final Boolean emailVerified = false;

    @JsonIgnore
    @Column
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column
    private String providerId;

    @Builder
    public User(Long id, String name, String email, String imageUrl, Role role, String password, AuthProvider provider, String providerId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
    }

    public User update(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        return this;
    }

}
