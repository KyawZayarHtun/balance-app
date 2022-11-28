package com.example.model.domain.vo;

import com.example.model.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVo {

    private int id;
    private String loginId;
    private String name;
    private boolean status;
    private String phone;
    private String email;

    public UserVo(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.status = user.isActive();
    }
}
