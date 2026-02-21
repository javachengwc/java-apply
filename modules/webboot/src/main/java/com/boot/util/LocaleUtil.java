package com.boot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class LocaleUtil {

    private static final Logger logger = LoggerFactory.getLogger(LocaleUtil.class);

    private static String LANGUAGE_KEY="language";

    private static String ZONE_KEY="zone";

    public static Locale getLocale() {
        Locale locale = null;
        try {
            String language = getLanguage();
            locale = StringUtils.parseLocaleString(language);
        } catch (Exception e) {
            locale = getLocaleFromContext();
            logger.error("获取语言出错,从request获取", e);
        }
        return locale;
    }

    public static Locale getLocaleFromContext() {
        RequestContext requestContext = new RequestContext(getRequest());
        return requestContext.getLocale();
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 语言
     */
    public static String getLanguage() {
        String value = WebUtil.getValueByServlet(LANGUAGE_KEY);
        return value;
    }

    /**
     * 区域
     */
    public static String getZone() {
        String value = WebUtil.getValueByServlet(ZONE_KEY);
        return value;
    }

}
