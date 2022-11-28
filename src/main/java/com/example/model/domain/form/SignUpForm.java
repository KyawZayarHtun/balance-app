package com.example.model.domain.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class SignUpForm {

    @NotBlank(message = "Enter Your Name!")
    private String name;
    @NotBlank(message = "Enter Login Id!")
    private String loginId;
    @NotBlank(message = "Enter password!")
    private String password;

}
