package com.configcenter.aop;

import com.configcenter.annotation.Cache;
import com.configcenter.service.manager.CacheManager;
import com.util.encrypt.MD5;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class CacheAspect {

    private static Logger logger = LoggerFactory.getLogger(CacheAspect.class);

    @Around(value="execution(* com.configcenter.service.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Exception {

        Object target = joinPoint.getTarget();
        Signature signature = joinPoint.getSignature();
        String serviceName =target.getClass().getSimpleName();
        Object result = null;
        try {
            MethodSignature methodSignature = null;
            if (!(signature instanceof MethodSignature)) {
                result = joinPoint.proceed();
                return  result;
            }
            methodSignature = (MethodSignature) signature;
            String methodName = methodSignature.getName();
            Class[] params =  methodSignature.getParameterTypes();
            Method curMethod = target.getClass().getMethod(methodName,params);
            Cache cache =curMethod.getAnnotation(Cache.class);
            if(cache==null) {
                result = joinPoint.proceed();
            }else {
                Object [] paramValues = joinPoint.getArgs();
                String cacheName =serviceName+"."+methodName+"_"+param2Str(paramValues);
                logger.info("CacheAspect around use cache,cacheName={}",cacheName);
                final ProceedingJoinPoint finalPoint = joinPoint;
                Object data = CacheManager.getDataWithCache(cacheName, new CacheManager.IQueryer() {
                    public Object query() throws Throwable {
                        return  finalPoint.proceed();
                    }
                });
                result = data;
            }
        } catch (Throwable e) {
            logger.info("CacheAspect around error,",e);
        }
        return result;
    }

    private String param2Str(Object []  values) {
        if(values==null || values.length<=0) {
            return "";
        }
        StringBuffer valueBuffer =new StringBuffer("");
        for(Object obj:values) {
            valueBuffer.append(obj.toString()).append("_");
        }
        String value =valueBuffer.toString();
        return MD5.getMD5(value);
    }
}
