package com.spring.pseudocode.web.web.method.annotation;

import com.spring.pseudocode.core.core.MethodParameter;
import com.spring.pseudocode.web.web.bind.support.WebDataBinderFactory;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;
import com.spring.pseudocode.web.web.method.HandlerMethod;
import com.spring.pseudocode.web.web.method.support.InvocableHandlerMethod;
import com.spring.pseudocode.web.web.method.support.ModelAndViewContainer;
import org.springframework.core.Conventions;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.SessionAttributesHandler;

import java.lang.reflect.Method;
import java.util.*;

public final class ModelFactory
{

    private final List<ModelMethod> modelMethods = new ArrayList<ModelMethod>();

    private final WebDataBinderFactory dataBinderFactory;

    private final SessionAttributesHandler sessionAttributesHandler;

    public ModelFactory(List<InvocableHandlerMethod> handlerMethods, WebDataBinderFactory binderFactory, SessionAttributesHandler attributeHandler)
    {
        if (handlerMethods != null) {
            for (InvocableHandlerMethod handlerMethod : handlerMethods) {
                this.modelMethods.add(new ModelMethod(handlerMethod));
            }
        }
        this.dataBinderFactory = binderFactory;
        this.sessionAttributesHandler = attributeHandler;
    }

    public void initModel(NativeWebRequest request, ModelAndViewContainer container, HandlerMethod handlerMethod) throws Exception
    {
         //伪代码
    }

    private void invokeModelAttributeMethods(NativeWebRequest request, ModelAndViewContainer container) throws Exception
    {
        while (!this.modelMethods.isEmpty()) {
            //InvocableHandlerMethod modelMethod = getNextModelMethod(container).getHandlerMethod();
            InvocableHandlerMethod modelMethod =null;
            String modelName = ((ModelAttribute)modelMethod.getMethodAnnotation(ModelAttribute.class)).value();
            if (container.containsAttribute(modelName))
            {
                continue;
            }
            Object returnValue = modelMethod.invokeForRequest(request, container, new Object[0]);
            if (!modelMethod.isVoid()) {
                String returnValueName = getNameForReturnValue(returnValue, modelMethod.getReturnType());
                if (!container.containsAttribute(returnValueName))
                    container.addAttribute(returnValueName, returnValue);
            }
        }
    }

    public static String getNameForReturnValue(Object returnValue, MethodParameter returnType)
    {
        ModelAttribute ann = (ModelAttribute)returnType.getMethodAnnotation(ModelAttribute.class);
        if ((ann != null) && (StringUtils.hasText(ann.value()))) {
            return ann.value();
        }

        Method method = returnType.getMethod();
        Class containingClass = returnType.getContainingClass();
        Class resolvedType = GenericTypeResolver.resolveReturnType(method, containingClass);
        return Conventions.getVariableNameForReturnType(method, resolvedType, returnValue);
    }

    private static class ModelMethod
    {
        private InvocableHandlerMethod handlerMethod;

        private final Set<String> dependencies = new HashSet<String>();

        private ModelMethod(InvocableHandlerMethod handlerMethod) {
            this.handlerMethod = handlerMethod;
            for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
//                if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
//                    this.dependencies.add(ModelFactory.getNameForParameter(parameter));
//                }
            }
        }

        public InvocableHandlerMethod getHandlerMethod()
        {
            return this.handlerMethod;
        }

    }
}