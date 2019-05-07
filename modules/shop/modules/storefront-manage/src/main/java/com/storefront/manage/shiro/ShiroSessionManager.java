package com.storefront.manage.shiro;

import com.shop.base.model.Req;
import com.shop.base.util.JsonUtil;
import com.util.web.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class ShiroSessionManager extends DefaultWebSessionManager {

    private static Logger logger= LoggerFactory.getLogger(ShiroSessionManager.class);

    private static final String TOKEN = "token";

    private static final String REFERENCED_SESSION_ID_SOURCE = "session source";

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 从Header中获取sessionId
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String sessionId = httpServletRequest.getHeader(TOKEN);
        if (StringUtils.isNotBlank(sessionId)) {
            setRequest(request, sessionId);
            return sessionId;
        }

        String body = RequestUtil.getBody(httpServletRequest);
        if (StringUtils.isNotBlank(body)) {
            try {
                Req reqst = JsonUtil.json2Obj(body, Req.class);
                if (reqst != null) {
                    sessionId = reqst.getHeader() == null ? null : reqst.getHeader().getToken();
                    if (StringUtils.isNotBlank(sessionId)) {
                        setRequest(request, sessionId);
                        return sessionId;
                    }
                }
            }catch (Exception e) {
                logger.error("ShiroSessionManager getSessionId  json2Obj error,body={}",body,e);
            }
        }
        return super.getSessionId(request, response);
    }

    private void setRequest(ServletRequest request, String sessionId) {
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
    }

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null && null != sessionId) {
            Object sessionObj = request.getAttribute(sessionId.toString());
            if (sessionObj != null) {
                return (Session) sessionObj;
            }
        }
        Session session = super.retrieveSession(sessionKey);
        if (request != null && null != sessionId) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }

}