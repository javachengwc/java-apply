package com.micro.user.service.impl;

import com.micro.user.service.LoginService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    public boolean checkLoginByToken(String token) {
        return true;
    }
}
