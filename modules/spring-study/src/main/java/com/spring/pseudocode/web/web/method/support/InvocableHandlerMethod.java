package com.spring.pseudocode.web.web.method.support;

import com.spring.pseudocode.core.core.MethodParameter;
import com.spring.pseudocode.web.web.bind.support.WebDataBinderFactory;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;
import com.spring.pseudocode.web.web.method.HandlerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

//具有请求方法调用功能的HandlerMethod
public class InvocableHandlerMethod extends HandlerMethod
{
    private static Logger logger= LoggerFactory.getLogger(InvocableHandlerMethod.class);

    private WebDataBinderFactory dataBinderFactory;

    private HandlerMethodArgumentResolverComposite argumentResolvers = new HandlerMethodArgumentResolverComposite();

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public InvocableHandlerMethod(HandlerMethod handlerMethod)
    {
        super(handlerMethod);
    }

    public InvocableHandlerMethod(Object bean, Method method)
    {
        super(bean, method);
    }

    public InvocableHandlerMethod(Object bean, String methodName, Class<?>[] parameterTypes) throws NoSuchMethodException
    {
        super(bean, methodName, parameterTypes);
    }

    public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory)
    {
        this.dataBinderFactory = dataBinderFactory;
    }

    public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite argumentResolvers)
    {
        this.argumentResolvers = argumentResolvers;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
    {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    private Object[] getMethodArgumentValues(NativeWebRequest request, ModelAndViewContainer mavContainer, Object[] providedArgs) throws Exception
    {
        MethodParameter[] parameters = getMethodParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            //parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            // GenericTypeResolver.resolveParameterType(parameter, getBean().getClass());
            args[i] = resolveProvidedArgument(parameter, providedArgs);
            if (args[i] != null) {
                continue;
            }
            if (this.argumentResolvers.supportsParameter(parameter)) {
                try {
                    //解析参数
                    args[i] = this.argumentResolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
                }
                catch (Exception ex)
                {
                    throw ex;
                }
            }
            else if (args[i] == null) {
                // String msg = getArgumentResolutionErrorMessage("No suitable resolver for argument", i);
                // throw new IllegalStateException(msg);
            }
        }
        return args;
    }

    private Object resolveProvidedArgument(MethodParameter parameter, Object[] providedArgs)
    {
        if (providedArgs == null) {
            return null;
        }
        for (Object providedArg : providedArgs) {
            if (parameter.getParameterType().isInstance(providedArg)) {
                return providedArg;
            }
        }
        return null;
    }

    //具体执行请求处理
    public Object invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer, Object[] providedArgs) throws Exception
    {
        Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
        if (this.logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder("Invoking [");
            sb.append(getBeanType().getSimpleName()).append(".");
            sb.append(getMethod().getName()).append("] method with arguments ");
            sb.append(Arrays.asList(args));
            this.logger.trace(sb.toString());
        }
        Object returnValue = doInvoke(args);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(new StringBuilder().append("Method [").append(getMethod().getName()).append("] returned [").append(returnValue).append("]").toString());
        }
        return returnValue;
    }

    //具体请求方法调用
    protected Object doInvoke(Object[] args) throws Exception {
        ReflectionUtils.makeAccessible(getBridgedMethod());
        Throwable targetException;
        String msg;
        try {
            return getBridgedMethod().invoke(getBean(), args);
        } catch (IllegalArgumentException ex)
        {
            assertTargetBean(getBridgedMethod(), getBean(), args);
            String message = ex.getMessage() != null ? ex.getMessage() : "Illegal argument";
            throw new IllegalStateException(getInvocationErrorMessage(message, args), ex);
        }
        catch (InvocationTargetException ex)
        {
            targetException = ex.getTargetException();
            if ((targetException instanceof RuntimeException)) {
                throw ((RuntimeException)targetException);
            }
            if ((targetException instanceof Error)) {
                throw ((Error)targetException);
            }
            if ((targetException instanceof Exception)) {
                throw ((Exception)targetException);
            }
            msg = getInvocationErrorMessage("Failed to invoke controller method", args);
            throw new IllegalStateException(msg, targetException);
        }
    }

    private void assertTargetBean(Method method, Object targetBean, Object[] args)
    {
        Class methodDeclaringClass = method.getDeclaringClass();
        Class targetBeanClass = targetBean.getClass();
        if (!methodDeclaringClass.isAssignableFrom(targetBeanClass))
        {
            String msg = new StringBuilder().append("The mapped controller method class '").append(methodDeclaringClass.getName())
                    .append("' is not an instance of the actual controller bean class '")
                    .append(targetBeanClass.getName()).append("'. If the controller requires proxying ")
                    .append("(e.g. due to @Transactional), please use class-based proxying.").toString();
            throw new IllegalStateException(getInvocationErrorMessage(msg, args));
        }
    }

    private String getInvocationErrorMessage(String message, Object[] resolvedArgs) {
        StringBuilder sb = new StringBuilder(getDetailedErrorMessage(message));
        sb.append("Resolved arguments: \n");
        for (int i = 0; i < resolvedArgs.length; i++) {
            sb.append("[").append(i).append("] ");
            if (resolvedArgs[i] == null) {
                sb.append("[null] \n");
            }
            else {
                sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
                sb.append("[value=").append(resolvedArgs[i]).append("]\n");
            }
        }
        return sb.toString();
    }

    protected String getDetailedErrorMessage(String message)
    {
        StringBuilder sb = new StringBuilder(message).append("\n");
        sb.append("HandlerMethod details: \n");
        sb.append("Controller [").append(getBeanType().getName()).append("]\n");
        sb.append("Method [").append(getBridgedMethod().toGenericString()).append("]\n");
        return sb.toString();
    }

}
