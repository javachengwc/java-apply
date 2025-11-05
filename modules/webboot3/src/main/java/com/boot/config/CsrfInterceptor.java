package com.boot.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.net.URL;
import java.util.stream.Stream;

/**
 * 跨域拦截器
 */
public class CsrfInterceptor implements HandlerInterceptor {

    private String[] domains;

    public CsrfInterceptor(String[] domains) {
        this.domains = domains;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referer = request.getHeader("referer");
        if(StringUtils.isNotEmpty(referer)) {
            URL url = new URL(referer);
            String host = url.getHost();
            if(ArrayUtils.isNotEmpty(this.domains) && !Stream.of(this.domains).anyMatch(item -> item.indexOf(host) > -1)) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
