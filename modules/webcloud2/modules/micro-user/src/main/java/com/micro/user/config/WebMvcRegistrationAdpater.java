package com.micro.user.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

//解决@FeignClient中的@RequestMapping也被SpringMVC加载的问题
@Configuration
public class WebMvcRegistrationAdpater implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new MyRequestMappingHandlerMapping();
    }

    public class MyRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

        @Override
        protected boolean isHandler(Class<?> beanType) {

            // 原来逻辑
            //return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) ||
            //        AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class));

            return super.isHandler(beanType) &&
                    !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
        }
    }
}
