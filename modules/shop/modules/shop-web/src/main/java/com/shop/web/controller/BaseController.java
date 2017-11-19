package com.shop.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BaseController {

    @Autowired
    private HttpServletRequest request;

    protected String getBaseUrl() {
        String path=request.getContextPath();
        String baseUrl=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        return baseUrl;
    }
}
