package com.micro.user.util;

import com.micro.user.constant.JwtConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {

    public static String generateToken(Long userId,String account, String randomStr) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(JwtConstant.MD5_KEY, randomStr);
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + JwtConstant.TOKEN_EEPIRATION);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(account)
                .setId(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JwtConstant.SECRET)
                .compact();
    }


}
