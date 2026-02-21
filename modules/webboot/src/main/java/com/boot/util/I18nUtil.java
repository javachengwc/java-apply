package com.boot.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

@Component
public class I18nUtil {

    private static Logger logger = LoggerFactory.getLogger(I18nUtil.class);

    @Resource(name="messageSource")
    private MessageSource messageSource;

    /**
     * 获取国际化值
     */
    public String getMessage(String msgKey) {
        try {
            return messageSource.getMessage(msgKey, null, LocaleUtil.getLocale());
        } catch (Exception e) {
            logger.error("getMessage error, msgKey={}",msgKey, e);
            return null;
        }
    }

    /**
     * 获取国际化值
     */
    public String getMessage(String msgKey, Locale locale) {
        try {
            return messageSource.getMessage(msgKey, null, locale);
        } catch (Exception e) {
            logger.error("getMessage error, msgKey={}",msgKey, e);
            return null;
        }
    }

    public static Locale getJvmLocale() {
        return new Locale(getJvmDefLang(), getJvmDefRegion());
    }

    // -Ddm_def_lang=zh
    public static String getJvmDefLang() {
        String value = System.getProperty("def_lang");
        if (StringUtils.isEmpty(value)) {
            return "zh";
        } else {
            return value;
        }
    }

    // -Ddm_def_region=CN
    public static String getJvmDefRegion() {
        String value = System.getProperty("def_region");
        if (StringUtils.isEmpty(value)) {
            return "CN";
        } else {
            return value;
        }
    }
}
