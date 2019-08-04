package com.micro.webcore.util;

import com.micro.webcore.constant.JwtConstant;
import com.micro.webcore.model.TokenInfo;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class TokenUtil {

    private static Logger logger= LoggerFactory.getLogger(TokenUtil.class);

    //提取token
    public static TokenInfo tipTokenInfo(HttpServletRequest request) {
        String token = request.getHeader(JwtConstant.HEADER_TOKEN);
        if (token == null || "".equals(token)) {
            return null;
        }
        TokenInfo tokenInfo= tipTokenInfo(token);
        return tokenInfo;
    }

    //提取token
    public static TokenInfo tipTokenInfo(String token) {
        TokenInfo tokenInfo= new TokenInfo();
        tokenInfo.setToken(token);
        try {
            Claims claims = JwtTokenUtil.getClaimFromToken(token);
            Date expiredDate = claims.getExpiration();
            Long userId = Long.valueOf(claims.getId());
            String mobile = claims.getSubject();
            tokenInfo.setUserId(userId);
            tokenInfo.setMobile(mobile);
            tokenInfo.setExpiredDate(expiredDate);
        }catch(Exception e) {
            logger.error("TokenUtil tipTokenInfo error,",e);
        }
        return tokenInfo;
    }
}
