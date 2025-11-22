package com.boot3.config;

import com.alibaba.fastjson.JSON;
import com.boot3.vo.RespVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private static final String URL_FAVICON = "/favicon.ico";
    private static final String URL_READY = "/ready";
    private static final String URL_HEALTH = "/health";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        Long userId = this.getUserId(request, response);
        return this.doAuthUrl(request, response, userId);
    }

    private Long getUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return 1L;
    }

    private boolean doAuthUrl(HttpServletRequest request, HttpServletResponse response, Long userId) throws Exception {
        String path = new UrlPathHelper().getLookupPathForRequest(request);
        // url鉴权时放过图标和健康检查的url
        if (path.equals(URL_FAVICON) || path.equals(URL_READY) || path.equals(URL_HEALTH)) {
            return true;
        }
        if (userId == null) {
            this.sendErrorMsg(request, response, "鉴权失败：获取当前登录用户失败");
            return false;
        }
        String url = request.getServerName() + path;
        if (this.authUrl(url, userId, request)) {
            return true;
        } else {
            log.warn("鉴权失败,无权限 请求url:{}:", url);
            this.sendErrorMsg(request, response, "权限不足，拒绝访问");
            return false;
        }
    }

    /**
     * 权限校验-url鉴权
     */
    private boolean authUrl(String url, Long userId, HttpServletRequest httpServletRequest) {
        return true;
    }

    /**
     * 鉴权失败，返回错误信息
     */
    private void sendErrorMsg(HttpServletRequest request, HttpServletResponse response,
                             String msg) throws Exception {
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.getOutputStream().write(JSON.toJSONString(RespVO.error(msg))
                .getBytes("UTF-8"));
    }

}
