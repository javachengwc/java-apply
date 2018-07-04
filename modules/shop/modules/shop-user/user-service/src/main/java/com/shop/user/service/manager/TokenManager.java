package com.shop.user.service.manager;

import com.shop.user.model.Token;
import com.shop.user.model.pojo.ShopUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenManager {

    private static Logger logger= LoggerFactory.getLogger(TokenManager.class);

    private static TokenManager inst = new TokenManager();

    public static TokenManager getInstance() {
        return inst;
    }

    private TokenManager() {
        init();
    }

    public void  init() {
        logger.info("TokenManager init start............");

        logger.info("TokenManager init end............");
    }

    //创建token
    public Token createToken(ShopUser user) {
        return null;
    }

    //获取token
    public Token getToken(String token) {
        return null;
    }

    //是否最新token
    public boolean isLatest(String token, Long userId) {
        return true;
    }

    //删除token
    public boolean deleteToken(String token) {
        return true;
    }

    //删除最新的token
    public boolean deleteLatest(Long userId) {
        return true;
    }
}
