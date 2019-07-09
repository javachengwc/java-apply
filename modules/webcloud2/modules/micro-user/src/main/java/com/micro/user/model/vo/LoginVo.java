package com.micro.user.model.vo;

import lombok.Data;

@Data
public class LoginVo {

    private Long userId;

    private String mobile;

    public LoginVo() {

    }

    public LoginVo(Long userId,String mobile) {
        this.userId=userId;
        this.mobile=mobile;
    }
}
