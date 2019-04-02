package com.shop.book.manage.aop;

import com.shop.base.model.Req;
import com.shop.base.util.JsonUtil;
import com.shop.book.manage.model.pojo.manage.User;
import com.shop.book.manage.shiro.ShiroManager;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Order(8)
@Component
public class CtrlAccessAspect {

    private static Logger logger = LoggerFactory.getLogger(CtrlAccessAspect.class);

    //忽略的请求参数类型
    private static List<Class> ignoreClassList = Stream.of(
            MultipartFile.class,
            Errors.class,
            ServletResponse.class,
            ServletRequest.class,
            HttpSession.class
    ).collect(Collectors.toList());

    private static String tokenKeyPart = "\"token\":\"";

    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    private ThreadLocal<String> accessId = new ThreadLocal<String>();

    private ThreadLocal<String> accessUrl = new ThreadLocal<String>();

    @Autowired
    private ShiroManager shiroManager;

    @Pointcut("execution(public * com.shop.book.manage.controller..*.*(..))")
    public void ctrlAccess() {
    }

    @Before("ctrlAccess()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        //controller名
        String controllerName = target.getClass().getSimpleName();
        long now = System.currentTimeMillis();
        startTime.set(now);
        String curAccessId = UUID.randomUUID().toString();
        //给访问打标
        accessId.set(curAccessId);
        //接收到请求
        String requestUrl="";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            requestUrl = request.getRequestURL().toString();
        }catch(Exception e) {
            logger.error("CtrlAccessAspect doBefore  get request error,accessId={},controllerName={}", curAccessId, controllerName,e);
            return;
        }
        accessUrl.set(requestUrl);

        Signature signature = joinPoint.getSignature();
        String methodName = null;
        Method currentMethod = null;
        Class[] paramClasses = null;
        Object[] paramValues = null;
        //关键业务参数
        Object bizValue = null;
        try {
            MethodSignature msig = (MethodSignature) signature;
            methodName = msig.getName();
            paramClasses = msig.getParameterTypes();
            currentMethod = target.getClass().getMethod(methodName, paramClasses);
            paramValues = joinPoint.getArgs();
            bizValue = tipBizParamValue(currentMethod, paramClasses, paramValues);
        } catch (Throwable e) {
            logger.error("CtrlAccessAspect doBefore tip param error,accessId={},controllerName={}", curAccessId, controllerName, e);
            return;
        }
        User user = fetchUser(bizValue);
        Long userId = user == null ? null : user.getId();
        String userName = user == null ? "" : user.getName();
        String bizValueStr = bizValue == null ? "" : JsonUtil.obj2Json(bizValue);
        //脱敏
        bizValueStr = dropSensitiveInfo(bizValueStr);
        logger.info("CtrlAccessAspect doBefore accessId={},requestUrl={},controllerName={},methodName={},operatorId={},operatorName={},biz param value={}",
                curAccessId, requestUrl, controllerName, methodName, userId, userName, bizValueStr);
    }

    @AfterReturning(returning = "ret", pointcut = "ctrlAccess()")
    public void doAfterReturning(Object ret) throws Throwable {
        Long beginTime = startTime.get();
        Long costTime = beginTime == null ? null : (System.currentTimeMillis() - beginTime);
        String curAccessId = accessId.get();
        String requestUrl = accessUrl.get();
        logger.info("CtrlAccessAspect doAfterReturning ,accessId={},requestUrl={},cost time={}", curAccessId, requestUrl, costTime);
    }

    //获取登陆用户
    public User fetchUser(Object bizValue) {
        try {
            if ((bizValue != null && (bizValue instanceof Req))) {
                User user = shiroManager.getLoginUser();
                return user;
            }
        } catch (Exception e) {
            logger.error("CtrlAccessAspect fetchUser error,", e);
        }
        return null;
    }

    //提取关键业务参数
    public Object tipBizParamValue(Method method, Class[] params, Object[] paramValues) {
        int paramLen = params == null ? 0 : params.length;
        int valueLen = paramValues == null ? 0 : paramValues.length;
        int minLen = Math.min(paramLen, valueLen);
        if (minLen <= 0) {
            return null;
        }
        Annotation[][] paramAnns = method.getParameterAnnotations();
        int paramAnnsLen = paramAnns == null ? 0 : paramAnns.length;
        Object bizObj = null;
        for (int i = 0; i < minLen; i++) {
            Class curClass = params[i];
            if (isIgnoreClass(curClass)) {
                continue;
            }
            Object curValue = paramValues[i];
            if (i == 0) {
                //默认取第一个参数值
                bizObj = curValue;
            }
            if ( Req.class.isAssignableFrom(curClass)) {
                //如果参数是Req这样的类型参数，直接当成关键业务参数返回;
                bizObj = curValue;
                return bizObj;
            }
            if (paramAnnsLen > i) {
                List<Annotation> anns = Arrays.asList(paramAnns[i]);
                if (anns.contains(RequestBody.class) || anns.contains(Validated.class) || anns.contains(Valid.class)) {
                    //如果参数有注解RequestBody 或Validated或 Valid，直接当成关键业务参数返回;
                    bizObj = curValue;
                    return bizObj;
                }
            }
        }
        return bizObj;
    }

    //去敏感信息
    public String dropSensitiveInfo(String bizValueStr) {
        if (StringUtils.isBlank(bizValueStr)) {
            return bizValueStr;
        }
        int tokenIndex = bizValueStr.indexOf(tokenKeyPart);
        if (tokenIndex < 0) {
            return bizValueStr;
        }
        tokenIndex = tokenIndex + tokenKeyPart.length();
        int afterIndex = bizValueStr.indexOf("\"", tokenIndex);
        if (afterIndex < tokenIndex) {
            return bizValueStr;
        }
        String value = bizValueStr.substring(0, tokenIndex) + bizValueStr.substring(afterIndex);
        return value;
    }

    private boolean isIgnoreClass(Class curClass) {
        for (Class ignoreClass : ignoreClassList) {
            if (ignoreClass.isAssignableFrom(curClass)) {
                return true;
            }
        }
        return false;
    }
}
