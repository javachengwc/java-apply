package com.micro.user.constant;

public class JwtConstant {

    public static String HEADER_TOKEN = "token";

    //token过期时间，30天
    public static long TOKEN_EEPIRATION = 30L*24*60*60;

    public static  String MD5_KEY = "randomKey";

    public static  String SECRET = "secret";
}
