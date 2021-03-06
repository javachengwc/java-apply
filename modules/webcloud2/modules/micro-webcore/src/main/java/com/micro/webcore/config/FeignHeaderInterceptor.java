package com.micro.webcore.config;


import com.micro.webcore.constant.HeaderConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
public class FeignHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(HeaderConstant.ACCESS_COMEFROM_HEARDER,HeaderConstant.ACCESS_COMEFROM_FEIGN);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if(request==null) {
            return;
        }
        Enumeration<String> hearders = request.getHeaderNames();
        while (hearders.hasMoreElements()) {
            String headerName = hearders.nextElement();
            String value = request.getHeader(headerName);
            // 把请求的header头 原样设置到feign请求头中
            template.header(headerName, value);
        }
    }
}
