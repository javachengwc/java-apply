package com.configcenter.service;

import com.configcenter.annotation.LogTag;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 日志记录切面
 */
@Component
@Aspect
public class LogAspect {

    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //@Pointcut("execution(* com.configcenter.service..update(..)) || execution(* com.configcenter.service..add(..))")
    @Pointcut("execution(* com.configcenter.service..*..*(..))")
    public void logPoint(){}


    @AfterReturning(pointcut="logPoint()", returning="returnValue")
    public void afterPonit(JoinPoint point, Object returnValue){

//        System.out.println("@AfterReturning：参数为：" +Arrays.toString(point.getArgs()));
//        System.out.println("@AfterReturning：返回值为：" + returnValue);

        String classMethod = point.getTarget().getClass().getSimpleName()+"."+point.getSignature().getName();

        if(needLog(point))
        {
            String operate= "执行"+classMethod;
            if(point.getArgs()!=null && point.getArgs().length>0)
            {
                Object data = point.getArgs()[0];
                operate+=",数据为:"+data;
            }
            LogManager.log(SessionManager.getCurUser(), operate);
        }
    }

    public boolean needLog(JoinPoint point)
    {

        String methodName =point.getSignature().getName();

        LogTag logTag = point.getTarget().getClass().getAnnotation(LogTag.class);
        if(logTag!=null)
        {
            String logMethods= logTag.value();
            if(!StringUtils.isBlank(logMethods))
            {
                String mtdArray [] = logMethods.split(",");
                for(String per:mtdArray)
                {
                    if(StringUtils.isBlank(per))
                    {
                        continue;
                    }
                    if(methodName.equals(per))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
