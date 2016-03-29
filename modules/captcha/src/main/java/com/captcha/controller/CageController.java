package com.captcha.controller;

import com.captcha.cage.GageTokenGenerator;
import com.github.cage.Cage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Cage生成验证码，小巧，好用
 */
@Controller
public class CageController {

    private static final Cage cage = new Cage(null,null,null,null,null, new GageTokenGenerator(),null);

    public static String generateToken() {

        String token = cage.getTokenGenerator().next();
        return token;
    }

    @ResponseBody
    @RequestMapping("getCage")
    public void getCage(HttpServletRequest request,HttpServletResponse response) throws Exception
    {

        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }
        String token =generateToken();
        session.setAttribute("captchaToken", token);
        setResponseHeaders(response);
        cage.draw(token, response.getOutputStream());

    }

    protected void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        long time = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }

}
