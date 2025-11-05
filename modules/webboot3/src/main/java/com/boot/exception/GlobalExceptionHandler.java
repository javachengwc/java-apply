package com.boot.exception;

import com.boot.vo.RespVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public RespVO exceptionHandler(HttpServletRequest request, Exception ex) {
        log.error(String.format("接口异常，uri:%s",request.getRequestURI()), ex);

        if (ex instanceof NumberFormatException || ex instanceof BindException
                || ex instanceof MethodArgumentTypeMismatchException) {
            return RespVO.error("参数类型不匹配");
        }
        return RespVO.error("系统异常：" + ex.getMessage());
    }
}
