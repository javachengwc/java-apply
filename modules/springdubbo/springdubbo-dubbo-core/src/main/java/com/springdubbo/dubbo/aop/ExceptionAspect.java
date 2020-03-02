package com.springdubbo.dubbo.aop;

import com.springdubbo.dubbo.exception.DubboException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.model.base.Resp;

/**
 * 异常处理切面
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class ExceptionAspect {

  /**
   * 异常处理切面方法
   * @param joinPoint 接入点
   * @return Object
   * @throws Throwable 异常
   */
  @Around("(execution(* com..*.provider..*.*(..)))")
  public Object exceptionAspect(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();
    String methodName = joinPoint.getSignature().getName();
    try {
      return joinPoint.proceed(args);
    } catch (DubboException de) {
      log.error("ExceptionAspect 检测到dubbo异常DubboException,exception methodName: {}, args: {}, code: {}, msg: {}",
          methodName,this.args2Str(args),de.getCode(), de.getMsg(), de);
      return Resp.error(de.getCode(),de.getMsg());
    } catch (Exception e) {
      log.error("ExceptionAspect dubbo invoke exception,methodName: {},args: {}", methodName, this.args2Str(args), e);
      return Resp.error(-1,"dubbo服务异常");
    }
  }

  private String args2Str(Object[] args ) {
    if(args==null ) {
      return null;
    }
    StringBuffer buffer= new StringBuffer("");
    for(Object arg:args) {
      buffer.append(arg==null?"null":arg.toString()).append(",");
    }
    return buffer.toString();
  }
}
