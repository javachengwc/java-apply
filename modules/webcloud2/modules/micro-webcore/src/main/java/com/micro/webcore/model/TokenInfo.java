package com.micro.webcore.model;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

@Data
public class TokenInfo {

    private String token;

    private Long userId;

    private String mobile;

    private Date expiredDate;

    public boolean hasCorrectToken() {
        boolean rt = StringUtils.isNotBlank(token) && userId!=null && mobile!=null && expiredDate!=null;
        return rt;
    }

    public boolean isExpired() {
        if(expiredDate==null) {
            return true;
        }
        Date now = new Date();
        //验证token是否过期
        if (expiredDate.before(now)) {
            return true;
        }
        return false;
    }
}
