package com.es.consumer.base.aop;


import com.es.consumer.base.config.SettingConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HandleMessageAspect {

    private static Logger logger = LoggerFactory.getLogger(HandleMessageAspect.class);

    @Around(value = "execution(* com.es.consumer..service.*.handleMessage(..))")
    public Object handleMessageAround(ProceedingJoinPoint joinPoint) throws Exception {
        Object target = joinPoint.getTarget();
        Signature signature = joinPoint.getSignature();
        String className = target.getClass().getSimpleName();

        try {
            MethodSignature methodSignature = (MethodSignature) signature;
            String methodName = methodSignature.getName();
            if (!SettingConfig.handleMessage) {
                logger.info("HandleMessageAspect around not handleMessage,className={},methodName={}", className, methodName);
                while (!SettingConfig.handleMessage) {
                    Thread.sleep(10 * 1000L);
                }
            }
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            logger.error("HandleMessageAspect around error,", e);
            throw (Exception) e;
        }
    }
}
