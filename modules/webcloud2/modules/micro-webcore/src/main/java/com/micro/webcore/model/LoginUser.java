package com.micro.webcore.model;

import lombok.Data;

@Data
public class LoginUser {

    private Long userId;

    private String mobile;

    public LoginUser() {

    }

    public LoginUser(Long userId, String mobile) {
        this.userId=userId;
        this.mobile=mobile;
    }
}
