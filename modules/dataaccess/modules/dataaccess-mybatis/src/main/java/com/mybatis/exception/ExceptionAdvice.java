package com.mybatis.exception;

import com.model.base.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 控制层所有Advice类，会捕获处理所有抛出的异常
 */
@RestControllerAdvice
public class ExceptionAdvice {

    private static Logger logger= LoggerFactory.getLogger(ExceptionAdvice.class);
    /**
     * 异常统一处理方法
     */
    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public Resp exceptionHandler(Throwable e) {
        logger.error("ExceptionAdvice exceptionHandler catch exception,",e);
        return Resp.error(-1,"服务异常");
    }
}

