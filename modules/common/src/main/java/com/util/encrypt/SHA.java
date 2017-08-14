package com.util.encrypt;

import com.util.CharsetUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * SHA1：160位二进制摘要（40位16进制字符串）（已破解）
 * SHA256：256位二进制摘要（64位16进制字符串）（常用）
 */
public class SHA {

    public static String  encodeSHA256(String data) throws Exception {
        return DigestUtils.sha256Hex(data.getBytes(CharsetUtil.UTF8));//SHA256
    }

    public static String  encodeSHA1(String data) throws Exception {
        return DigestUtils.sha1Hex(data.getBytes(CharsetUtil.UTF8));//SHA1
    }

    public static void main(String args []) throws Exception {

        String test ="test";
        System.out.println(SHA.encodeSHA1(test));
        System.out.println("---------------------------------");
        System.out.println(SHA.encodeSHA256(test));
    }

}
