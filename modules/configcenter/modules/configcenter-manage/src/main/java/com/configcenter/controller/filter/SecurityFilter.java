package com.configcenter.controller.filter;


import com.configcenter.constant.Constant;
import com.configcenter.service.SessionManager;
import com.configcenter.vo.OnlineUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限过滤器
 */
public class SecurityFilter implements Filter{

    private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    private String noSessionAccess="login.do,menuList.do";

    public String getNoSessionAccess() {
        return noSessionAccess;
    }

    public void setNoSessionAccess(String noSessionAccess) {
        this.noSessionAccess = noSessionAccess;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		HttpServletResponse hresponse =(HttpServletResponse) response;
		HttpServletRequest hrequest =(HttpServletRequest) request;
		String url=hrequest.getRequestURL().toString();

		if(noNeedSessionAccess(url))
		{
            chain.doFilter(request, response);
			return;
		}

        String sessionKey =  Constant.USER_SESSION_ACCOUNT;
        String account =( hrequest.getSession().getAttribute(sessionKey)==null)?"": hrequest.getSession().getAttribute(sessionKey).toString();

		if(StringUtils.isBlank(account) || SessionManager.getInstance().getOnlineUser(account)==null )
		{
            logger.info("SecurityFilter no session,url="+url);
			hresponse.sendRedirect(hrequest.getContextPath());
			return;
		}

        OnlineUser onlineUser = SessionManager.getInstance().getOnlineUser(account);

        SessionManager.setCurUser(onlineUser);
		
		chain.doFilter(request, response);

        SessionManager.cleanCurUser();
	}

    //判断是否无需会话态访问，true--不需要  false--需要
    private boolean noNeedSessionAccess(String url)
    {
        if(StringUtils.isBlank(noSessionAccess))
        {
            return false;
        }
        String strArray [] = noSessionAccess.split(",");
        for(String per:strArray)
        {
            if(StringUtils.isBlank(per))
            {
                continue;
            }
            if(url.indexOf(per)!=-1)
            {
                return true;
            }
        }
        return false;
    }

}
