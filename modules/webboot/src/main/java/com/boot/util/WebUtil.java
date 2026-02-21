package com.boot.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtil {

    public static String getValueByServlet(String key) {
        String value = null;
        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        } else {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            if (request == null) {
                return null;
            } else {
                value = request.getHeader(key);
                if (value != null && value.length() > 0) {
                    return value;
                } else {
                    Cookie[] cookies = request.getCookies();
                    if (cookies == null) {
                        return value;
                    } else {
                        for(Cookie cookie : cookies) {
                            if (cookie.getName().equals(key)) {
                                value = cookie.getValue();
                                break;
                            }
                        }

                        return value;
                    }
                }
            }
        }
    }
}
