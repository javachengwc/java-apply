package com.spring.pseudocode.web.web.context.request;

import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.ClassUtils;

public abstract class RequestContextHolder
{
    //是否有FacesContext类存在
    private static final boolean jsfPresent = ClassUtils.isPresent("javax.faces.context.FacesContext",
            RequestContextHolder.class.getClassLoader());

    private static final ThreadLocal<RequestAttributes> requestAttributesHolder = new NamedThreadLocal("Request attributes");

    private static final ThreadLocal<RequestAttributes> inheritableRequestAttributesHolder = new NamedInheritableThreadLocal("Request context");

    public static void resetRequestAttributes()
    {
        requestAttributesHolder.remove();
        inheritableRequestAttributesHolder.remove();
    }

    public static void setRequestAttributes(RequestAttributes attributes)
    {
        setRequestAttributes(attributes, false);
    }

    public static void setRequestAttributes(RequestAttributes attributes, boolean inheritable)
    {
        if (attributes == null) {
            resetRequestAttributes();
        }
        else if (inheritable) {
            inheritableRequestAttributesHolder.set(attributes);
            requestAttributesHolder.remove();
        }
        else {
            requestAttributesHolder.set(attributes);
            inheritableRequestAttributesHolder.remove();
        }
    }

    public static RequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = (RequestAttributes)requestAttributesHolder.get();
        if (attributes == null) {
            attributes = (RequestAttributes)inheritableRequestAttributesHolder.get();
        }
        return attributes;
    }

    public static RequestAttributes currentRequestAttributes() throws IllegalStateException
    {
        RequestAttributes attributes = getRequestAttributes();
        if (attributes == null) {
            if (jsfPresent) {
                attributes = FacesRequestAttributesFactory.getFacesRequestAttributes();
            }
            if (attributes == null) {
                throw new IllegalStateException("No thread-bound request found......");
            }
        }
        return attributes;
    }

    private static class FacesRequestAttributesFactory
    {
        public static RequestAttributes getFacesRequestAttributes()
        {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            return facesContext != null ? new FacesRequestAttributes(facesContext) : null;
            return null;
        }
    }
}
