package com.shop.book.aop;

import com.shop.book.annotation.DataCache;
import com.shop.book.service.manager.CacheManager;
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

    @Around(value="execution(* com.shop.book.service.*.*(..))")
    public Object aroundMethod(ProceedingJoinPoint jp) throws Exception {

        Object target = jp.getTarget();
        String serviceName =target.getClass().getSimpleName();
        Signature signature = jp.getSignature();
        Object result = null;
        try {
            MethodSignature mdSgn = null;
            if (!(signature instanceof MethodSignature)) {
                result = jp.proceed();
                return  result;
            }
            mdSgn = (MethodSignature) signature;
            String methodName = mdSgn.getName();
            Class[] params =  mdSgn.getParameterTypes();
            Method currentMethod = target.getClass().getMethod(mdSgn.getName(),params);
            DataCache cache = (DataCache)currentMethod.getAnnotation(DataCache.class);
            if(cache==null) {
                result = jp.proceed();
            }else {
                Object [] paramValues = jp.getArgs();
                String cacheName =serviceName+"."+methodName+"_"+paramValue2Str(paramValues);
                logger.info("CacheAspect aroundMethod use cache ,serviceName={},methodName={},cacheName={}",serviceName,methodName,cacheName);
                Object data = CacheManager.getDataWithCache(cacheName, new CacheManager.IQueryer() {
                    public Object queryData() throws Throwable {
                        return  jp.proceed();
                    }
                });
                result = data;
            }
        } catch (Throwable e) {
            logger.info("CacheAspect aroundMethod error,",e);
        }
        return result;
    }

    private String paramValue2Str(Object []  values) {
        if(values==null || values.length<=0) {
            return "";
        }
        StringBuffer valueBuf =new StringBuffer("");
        for(Object obj:values) {
            if(obj==null) {
                valueBuf.append("_");
            } else {
                valueBuf.append(obj.toString()).append("_");
            }
        }
        String value =valueBuf.toString();
        return MD5.getMD5(value);
    }
}

