package com.spring.pseudocode.webmvc.mvc.method.annotation;


import com.spring.pseudocode.webmvc.mvc.method.RequestMappingInfo;
import com.spring.pseudocode.webmvc.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;

//Spring MVC处理请求映射
public class RequestMappingHandlerMapping extends RequestMappingInfoHandlerMapping
{
    private boolean useSuffixPatternMatch = true;

    private boolean useRegisteredSuffixPatternMatch = false;

    private boolean useTrailingSlashMatch = true;

    private StringValueResolver embeddedValueResolver;

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element)
    {
        RequestMapping requestMapping = (RequestMapping) AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);

        RequestCondition condition = (element instanceof Class) ?
                getCustomTypeCondition((Class)element)
                : getCustomMethodCondition((Method)element);
        return requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null;
    }

    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType)
    {
        return null;
    }

    protected RequestCondition<?> getCustomMethodCondition(Method method)
    {
        return null;
    }

    protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, RequestCondition<?> customCondition)
    {
        return null;
//        return RequestMappingInfo.paths(resolveEmbeddedValuesInPatterns(requestMapping
//                .path()))
//                .methods(requestMapping
//                        .method())
//                .params(requestMapping
//                        .params())
//                .headers(requestMapping
//                        .headers())
//                .consumes(requestMapping
//                        .consumes())
//                .produces(requestMapping
//                        .produces())
//                .mappingName(requestMapping
//                        .name())
//                .customCondition(customCondition)
//                .build();
    }

    protected String[] resolveEmbeddedValuesInPatterns(String[] patterns)
    {
        if (this.embeddedValueResolver == null) {
            return patterns;
        }
        String[] resolvedPatterns = new String[patterns.length];
        for (int i = 0; i < patterns.length; i++) {
            resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
        }
        return resolvedPatterns;
    }

    //判断是否要处理请求映射
    //默认@Controller,@RequestMapping注解的类会进行处理请求映射
    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) || AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class);
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method paramMethod, Class<?> paramClass) {
        return null;
    }

    @Override
    protected Set<String> getMappingPathPatterns(RequestMappingInfo paramT) {
        return null;
    }

    @Override
    protected RequestMappingInfo getMatchingMapping(RequestMappingInfo paramT, HttpServletRequest paramHttpServletRequest) {
        return null;
    }

    @Override
    protected Comparator<RequestMappingInfo> getMappingComparator(HttpServletRequest paramHttpServletRequest) {
        return null;
    }

    public int getOrder() {
        return 0;
    }
}
