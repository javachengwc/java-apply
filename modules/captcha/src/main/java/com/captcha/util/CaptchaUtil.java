package com.captcha.util;

import org.apache.commons.lang3.StringUtils;

public class CaptchaUtil {

    public static final String CAPTCHA_KEY = "_captcha";

    public static boolean isCaptchaCorrect(String captcha, String sessionId) {
        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(sessionId)) {
            return false;
        }
        String correctCaptcha = MemcachedUtil.get(sessionId + CAPTCHA_KEY);
        if (captcha.equalsIgnoreCase(correctCaptcha)) {
            return true;
        }
        return false;
    }
}
