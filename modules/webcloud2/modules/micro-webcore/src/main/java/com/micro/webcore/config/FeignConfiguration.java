package com.micro.webcore.config;

import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


//使用feign调用，有一个情况，定义在服务提供方的那些请求映射关系也被加载到了服务消费者中，这就会带来两个问题：
//1,由于服务消费者并不提供这些接口，对于开发者来说容易造成误解
//2.由于加载了一些外部服务的接口定义，还存在与自身接口定义冲突的潜在风险
//解决方法:扩展RequestMappingHandlerMapping,重写isHandler()方法
//如下实现的isHandler方法继承了原来的实现，同时增加了一个条件：不能被@FeignClient注解修饰的类才会进行解析加载。
@Configuration
@ConditionalOnClass({Feign.class})
public class FeignConfiguration {

    @Bean
    public WebMvcRegistrations feignWebRegistrations() {

        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new FeignRequestMappingHandlerMapping();
            }
        };
    }

    private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected boolean isHandler(Class<?> beanType) {
            return super.isHandler(beanType) &&
                    !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
        }
    }
}