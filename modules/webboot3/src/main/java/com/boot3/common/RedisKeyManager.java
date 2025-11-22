package com.boot3.common;

public final class RedisKeyManager {
    //默认过期时间(秒)
    public final static int DEFAULT_EXPIRE = 60;
    //用户角色缓存
    public final static String USER_ROLE = "userRole";
    //用户角色缓存300秒
    public final static int USER_ROLE_EXPIRE = 300;
}

