package com.micro.user.interceptor;

import com.model.base.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public Resp<Object> errorHandler(Exception e) {
        logger.info("GlobalExceptionHandler errorHandler start ,param exception={}", e);
        return Resp.error("服务异常");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Resp<Object> argFailHandler(MethodArgumentNotValidException e) {
        Resp<Object> resp = Resp.error("参数校验不通过");
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> errors = bindingResult==null?null:bindingResult.getAllErrors();
        if (errors != null && errors.size() > 0) {
            resp= Resp.error("参数校验不通过,"+errors.get(0).getDefaultMessage());
        }
        return resp;
    }

}
