package com.component.rest.interceptor;


import com.component.rest.filter.BalanceClientFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RestTemplateHeaderInterceptor implements ClientHttpRequestInterceptor {

    private static Logger logger = LoggerFactory.getLogger(RestTemplateHeaderInterceptor.class);

    @Value("${spring.application.name:noname-service}")
    private String appName;

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        try {
            Map<String, String> headerHolder = new HashMap<String, String>();
            BalanceClientFilter.copyTo(headerHolder);
            if (!headerHolder.isEmpty()) {
                request.getHeaders().setAll(headerHolder);
            }
            request.getHeaders().add(BalanceClientFilter.APP_NAME_KEY, appName);
        } catch (Exception e) {
            logger.warn("RestTemplateHeaderInterceptor intercept restTemplate add header error.");
        }

        String restConsistent =request.getHeaders().getFirst(BalanceClientFilter.REST_CONSISTENT_KEY);
        BalanceClientFilter.setRestConsistent(restConsistent);
        try{
            return execution.execute(request, body);
        }finally{
            BalanceClientFilter.removeRestConsistent();
        }
    }

}

