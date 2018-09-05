package com.spring.pseudocode.aop.aop.framework;

import com.spring.pseudocode.aop.aop.Advisor;
import com.spring.pseudocode.aop.aop.IntroductionAdvisor;
import com.spring.pseudocode.aop.aop.MethodMatcher;
import com.spring.pseudocode.aop.aop.PointcutAdvisor;
import com.spring.pseudocode.aop.aop.framework.adapter.AdvisorAdapterRegistry;
import com.spring.pseudocode.aop.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import com.spring.pseudocode.aop.aopalliance.intercept.Interceptor;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInterceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  DefaultAdvisorChainFactory implements AdvisorChainFactory, Serializable
{
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised config, Method method, Class<?> targetClass)
    {
        List interceptorList = new ArrayList(config.getAdvisors().length);
        Class actualClass = targetClass != null ? targetClass : method.getDeclaringClass();
        boolean hasIntroductions = hasMatchingIntroductions(config, actualClass);
        AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();

        for (Advisor advisor : config.getAdvisors()) {
            if ((advisor instanceof PointcutAdvisor))
            {
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor)advisor;
                if ((config.isPreFiltered()) || (pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass))) {
                    MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
                    MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
//                    if (MethodMatchers.matches(mm, method, actualClass, hasIntroductions)) {
//                        if (mm.isRuntime())
//                        {
//                            for (MethodInterceptor interceptor : interceptors) {
//                                interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
//                            }
//                        }
//                        else {
//                            interceptorList.addAll(Arrays.asList(interceptors));
//                        }
//                    }
                }
            }
            else if ((advisor instanceof IntroductionAdvisor)) {
                IntroductionAdvisor ia = (IntroductionAdvisor)advisor;
                if ((config.isPreFiltered()) || (ia.getClassFilter().matches(actualClass))) {
                    Interceptor[] interceptors = registry.getInterceptors(advisor);
                    interceptorList.addAll(Arrays.asList(interceptors));
                }
            }
            else {
                Interceptor[] interceptors = registry.getInterceptors(advisor);
                interceptorList.addAll(Arrays.asList(interceptors));
            }
        }

        return interceptorList;
    }

    private static boolean hasMatchingIntroductions(Advised config, Class<?> actualClass)
    {
        for (int i = 0; i < config.getAdvisors().length; i++) {
            Advisor advisor = config.getAdvisors()[i];
            if ((advisor instanceof IntroductionAdvisor)) {
                IntroductionAdvisor ia = (IntroductionAdvisor)advisor;
                if (ia.getClassFilter().matches(actualClass)) {
                    return true;
                }
            }
        }
        return false;
    }
}
