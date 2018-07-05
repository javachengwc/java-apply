package com.shop.user.service.manager;

import com.shop.user.model.Token;
import com.shop.user.model.pojo.ShopUser;
import com.shop.user.util.SpringContextUtils;
import com.util.col.MapUtil;
import com.util.date.CalendarUtil;
import com.util.enh.DataTransUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TokenManager {

    private static Logger logger= LoggerFactory.getLogger(TokenManager.class);

    private static int TOKEN_EXPIRE_DAY=30;

    //缓存key前缀
    private String CACHE_TOKEN_PREFIX="login_token_";

    private String CACHE_USER_PREFIX="login_user_";

    private static TokenManager inst = new TokenManager();

    public static TokenManager getInstance() {
        return inst;
    }

    private RedisTemplate redisTemplate;

    private TokenManager() {
        init();
    }

    public void  init() {
        logger.info("TokenManager init start............");
        redisTemplate = SpringContextUtils.getBean("redisTemplate",RedisTemplate.class);
        if(redisTemplate==null) {
            logger.warn("TokenManager init  redisTemplate is null......");
        }
        logger.info("TokenManager init end............");
    }

    //创建token
    public Token createToken(ShopUser user) {
        Date now = new Date();
        Token token = new Token();
        Long userId= user.getId();
        token.setUserId(userId);
        String tokenStr =  UUID.randomUUID().toString();
        token.setToken(tokenStr);
        token.setDeviceToken(user.getDeviceToken());
        token.setCreateTime(now);
        Date expireDate = CalendarUtil.addDates(now,TOKEN_EXPIRE_DAY);
        token.setExpireTime(expireDate);
        Map<String,Object> tokenMap = DataTransUtil.bean2Map(token);

        //token放缓存
        String tokenKey =CACHE_TOKEN_PREFIX+tokenStr;
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(tokenKey, tokenMap);
        redisTemplate.expire(tokenKey, TOKEN_EXPIRE_DAY, TimeUnit.DAYS);

        //user-token放缓存
        String userKey =CACHE_USER_PREFIX+userId;
        redisTemplate.opsForValue().set(userKey, tokenStr, TOKEN_EXPIRE_DAY, TimeUnit.DAYS);

        logger.info("TokenManager createToken success,userId={},token={}", userId,tokenStr);
        return token;
    }

    //获取token
    public Token getToken(String token) {
        String tokenKey =CACHE_TOKEN_PREFIX+token;
        HashOperations hashOperations = redisTemplate.opsForHash();
        Map<String,Object> tokenMap =hashOperations.entries(tokenKey);
        if(tokenMap==null || tokenMap.size()<=0) {
            return null;
        }
        Token tokenObj =MapUtil.map2Bean(tokenMap,Token.class);
        return tokenObj;
    }

    //是否最新token
    public boolean isLatest(String token, Long userId) {
        if (!StringUtils.isBlank(token) && userId!=null) {
            String userKey =CACHE_USER_PREFIX+userId;
            Object tokenObj = redisTemplate.opsForValue().get(userKey);
            String tokenStr = tokenObj==null?null:tokenObj.toString();
            if (!StringUtils.isBlank(tokenStr) && token.equals(tokenStr)) {
                return true;
            }
            return false;
        }
        return true;
    }

    //删除token
    public boolean deleteToken(String token) {
        if (!StringUtils.isBlank(token)) {
            String tokenKey =CACHE_TOKEN_PREFIX+token;
            redisTemplate.delete(tokenKey);
            return true;
        }
        return true;
    }

    //删除最新的token
    public boolean deleteLatest(Long userId) {
        if (userId!=null) {
            String userKey =CACHE_USER_PREFIX+userId;
            redisTemplate.delete(userKey);
            return true;
        }
        return true;
    }
}
