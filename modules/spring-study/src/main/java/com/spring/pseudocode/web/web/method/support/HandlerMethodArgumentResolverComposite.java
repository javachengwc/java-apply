package com.spring.pseudocode.web.web.method.support;

import com.spring.pseudocode.core.core.MethodParameter;
import com.spring.pseudocode.web.web.bind.support.WebDataBinderFactory;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//参数解析综合器
public class HandlerMethodArgumentResolverComposite  implements HandlerMethodArgumentResolver
{
    private final List<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<HandlerMethodArgumentResolver>();

    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap(256);

    public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver resolver)
    {
        this.argumentResolvers.add(resolver);
        return this;
    }

    public HandlerMethodArgumentResolverComposite addResolvers(List<? extends HandlerMethodArgumentResolver> resolvers)
    {
        if (resolvers != null) {
            for (HandlerMethodArgumentResolver resolver : resolvers) {
                this.argumentResolvers.add(resolver);
            }
        }
        return this;
    }

    public List<HandlerMethodArgumentResolver> getResolvers()
    {
        return Collections.unmodifiableList(this.argumentResolvers);
    }

    public boolean supportsParameter(MethodParameter parameter)
    {
        return getArgumentResolver(parameter) != null;
    }

    //解析参数
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception
    {
        HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
        if (resolver == null) {
            throw new IllegalArgumentException("Unknown parameter type [" + parameter.getParameterType().getName() + "]");
        }
        return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }

    private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter)
    {
        HandlerMethodArgumentResolver result = (HandlerMethodArgumentResolver)this.argumentResolverCache.get(parameter);
        if (result == null) {
            for (HandlerMethodArgumentResolver methodArgumentResolver : this.argumentResolvers) {
                if (methodArgumentResolver.supportsParameter(parameter)) {
                    result = methodArgumentResolver;
                    this.argumentResolverCache.put(parameter, result);
                    break;
                }
            }
        }
        return result;
    }
}