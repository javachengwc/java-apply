package com.spring.pseudocode.webmvc.config.annotation;


import java.util.ArrayList;
import java.util.List;

import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;


public class WebMvcConfigurerComposite implements WebMvcConfigurer {

    private final List<WebMvcConfigurer> delegates = new ArrayList<WebMvcConfigurer>();


    public void addWebMvcConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.delegates.addAll(configurers);
        }
    }


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.configurePathMatch(configurer);
        }
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.configureContentNegotiation(configurer);
        }
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.configureAsyncSupport(configurer);
        }
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.configureDefaultServletHandling(configurer);
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.addFormatters(registry);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.addInterceptors(registry);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.addResourceHandlers(registry);
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.addCorsMappings(registry);
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.addViewControllers(registry);
        }
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.configureViewResolvers(registry);
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.addArgumentResolvers(argumentResolvers);
        }
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.addReturnValueHandlers(returnValueHandlers);
        }
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.configureMessageConverters(converters);
        }
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.extendMessageConverters(converters);
        }
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.configureHandlerExceptionResolvers(exceptionResolvers);
        }
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.extendHandlerExceptionResolvers(exceptionResolvers);
        }
    }

    @Override
    public Validator getValidator() {
        Validator selected = null;
        for (WebMvcConfigurer configurer : this.delegates) {
            Validator validator = configurer.getValidator();
            if (validator != null) {
                if (selected != null) {
                    throw new IllegalStateException("No unique Validator found: {" +
                            selected + ", " + validator + "}");
                }
                selected = validator;
            }
        }
        return selected;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        MessageCodesResolver selected = null;
        for (WebMvcConfigurer configurer : this.delegates) {
            MessageCodesResolver messageCodesResolver = configurer.getMessageCodesResolver();
            if (messageCodesResolver != null) {
                if (selected != null) {
                    throw new IllegalStateException("No unique MessageCodesResolver found: {" +
                            selected + ", " + messageCodesResolver + "}");
                }
                selected = messageCodesResolver;
            }
        }
        return selected;
    }

}

