package com.captcha.controller;

import com.captcha.service.CaptchaService;
import com.captcha.service.PatchcaService;
import com.github.bingoohuang.patchca.service.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * patchca生成多彩验证码
 */
@Controller
public class PatchcaOrglController {

    @Autowired
    private PatchcaService patchcaService;

    @RequestMapping("/getPatchca2")
    @ResponseBody
    public void getPatchca2(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        HttpSession session = request.getSession(true);
        Captcha captcha = patchcaService.getCaptcha();

        // 取得验证码字符串放入Session
        String validationCode = captcha.getChallenge();
        session.setAttribute("validationCode", validationCode);

        // 取得验证码图片并输出

        BufferedImage bufferedImage = captcha.getImage();
        output(bufferedImage,response);
    }

    public void output(BufferedImage image ,HttpServletResponse response)
    {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        ServletOutputStream sos = null;
        try {
            sos = response.getOutputStream();
            ImageIO.write(image, "jpeg", sos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                sos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
