package com.micro.webcore.config;

import com.micro.webcore.constant.HeaderConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

//restTemplate的请求头包装
@Component
public class RestHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(HeaderConstant.ACCESS_COMEFROM_HEARDER, HeaderConstant.ACCESS_COMEFROM_REST);
        HttpServletRequest orglrequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if(orglrequest!=null) {
            Enumeration<String> hearders = orglrequest.getHeaderNames();
            while (hearders.hasMoreElements()) {
                String headerName = hearders.nextElement();
                String value = orglrequest.getHeader(headerName);
                // 把请求的header头 原样设置到restTemplate请求头中
                headers.add(headerName, value);
            }
        }
        return execution.execute(request, body);
    }
}