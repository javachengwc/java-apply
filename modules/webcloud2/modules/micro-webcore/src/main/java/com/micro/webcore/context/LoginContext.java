package com.micro.webcore.context;

import com.micro.webcore.constant.JwtConstant;
import com.micro.webcore.model.LoginUser;
import com.micro.webcore.util.JwtTokenUtil;
import com.shop.base.model.Resp;
import com.shop.base.util.JsonUtil;
import com.util.web.HttpRenderUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class LoginContext {

    public static LoginUser getLoginUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object obj =request.getAttribute("loginUser");
        if(obj==null) {
            String headerTokenValue = request.getHeader(JwtConstant.HEADER_TOKEN);
            if (StringUtils.isBlank(headerTokenValue)) {
                return null;
            }
            Claims claims = JwtTokenUtil.getClaimFromToken(headerTokenValue);
            Date expiredDate = claims.getExpiration();
            Date now = new Date();
            //验证token是否过期
            if (expiredDate.before(now)) {
                return  null;
            }
            Long userId = Long.valueOf(claims.getId());
            LoginUser userVo = new LoginUser(userId, claims.getSubject());
            return userVo;
        }
        LoginUser userVo = (LoginUser)obj;
        return userVo;
    }
}
