package com.example.model.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserAccessLog implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private Type type;
    private LocalDateTime accessTime;
    private String errorMessage;

    public enum Type {
        Signin, Signout, Error
    }

    public UserAccessLog(String username, Type type, LocalDateTime accessTime) {
        this.username = username;
        this.type = type;
        this.accessTime = accessTime;
    }

    public UserAccessLog(String username, Type type, LocalDateTime accessTime, String errorMessage) {
        this.username = username;
        this.type = type;
        this.accessTime = accessTime;
        this.errorMessage = errorMessage;
    }

    
}
