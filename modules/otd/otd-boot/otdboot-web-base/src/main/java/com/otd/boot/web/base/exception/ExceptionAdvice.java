package com.otd.boot.web.base.exception;

import com.otd.boot.web.base.model.vo.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 控制层所有Advice类，会捕获处理所有抛出的异常
 */
@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

  /**
   * 异常统一处理方法
   */
  @ExceptionHandler({Throwable.class})
  @ResponseBody
  public Resp exceptionHandler(Throwable e) {
    log.error("ExceptionAdvice exceptionHandler catch exception,",e);
    return Resp.error(-1,"服务异常");
  }
}
