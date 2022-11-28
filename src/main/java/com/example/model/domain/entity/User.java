package com.example.model.domain.entity;

import com.example.model.domain.form.SignUpForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false, unique = true)
    private String loginId;
    @Column(nullable = false)
    private String password;
    private String phone;
    private String email;
    private boolean active;

    public User(SignUpForm signUp) {
        this.name = signUp.getName();
        this.loginId = signUp.getLoginId();
        this.password = signUp.getPassword();
        this.active = true;
        this.role = Role.MEMBER;
    }

    public enum Role {
        ADMIN, MEMBER
    }

}
