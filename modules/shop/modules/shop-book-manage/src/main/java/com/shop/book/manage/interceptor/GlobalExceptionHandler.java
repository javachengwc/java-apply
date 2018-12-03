package com.shop.book.manage.interceptor;

import com.shop.base.model.Resp;
import com.shop.book.manage.enums.ApiCode;
import org.apache.shiro.authz.UnauthorizedException;
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
        Resp<Object> resp = Resp.error(ApiCode.UNKOWN_FAIL.getCode(),ApiCode.UNKOWN_FAIL.getMessage());
        if (e instanceof UnauthorizedException){
            resp= Resp.error(ApiCode.UNAUTH.getCode(),ApiCode.UNAUTH.getMessage());
        }
        return resp;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Resp<Object> argFailHandler(MethodArgumentNotValidException e) {
        Resp<Object> resp = Resp.error(ApiCode.PARAM_FAIL.getCode(),ApiCode.PARAM_FAIL.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();
        if (errors != null && errors.size() > 0) {
            resp= Resp.error(ApiCode.PARAM_FAIL.getCode(), errors.get(0).getDefaultMessage());
        }
        return resp;
    }

}
