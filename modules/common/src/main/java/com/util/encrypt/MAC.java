package com.util.encrypt;

import com.util.CharsetUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacUtils;

/**
 * mac(又称hmac)是在md与sha系列算法基础上加入了密钥，是常用消息摘要算法中最安全的
 * mac算法：HmacMD5,HmacSHA1,HmacSHA256
 */
public class MAC {

    //密钥
    public static byte[] getKey() throws Exception{
        return Hex.decodeHex(new char[]{'a', 'b', 'c', 'd'});
    }

    //HmacMD5加密
    public static byte[] encodeHmacMD5(String data, byte[] keyByte) throws Exception{
        return HmacUtils.hmacMd5(keyByte, data.getBytes(CharsetUtil.UTF8));
    }

    //HmacMD5加密，并转为16进制
    public static String encodeHmacMD5Hex(String data, byte[] keyByte) throws Exception{
        return HmacUtils.hmacMd5Hex(keyByte, data.getBytes(CharsetUtil.UTF8));
    }

    //HmacSha1加密，并转为16进制
    public static String encodeHmacSha1Hex(String data, byte[] keyByte) throws Exception{
        return HmacUtils.hmacSha1Hex(keyByte, data.getBytes(CharsetUtil.UTF8));
    }

    //HmacSha256加密，并转为16进制
    public static String encodeHmacSha256Hex(String data, byte[] keyByte) throws Exception{
        return HmacUtils.hmacSha256Hex(keyByte, data.getBytes(CharsetUtil.UTF8));
    }

    public static void main(String args []) throws Exception {

        String test ="test";
        System.out.println(MAC.encodeHmacMD5Hex(test,getKey()));
        System.out.println("---------------------------------");
        System.out.println(MAC.encodeHmacSha1Hex(test,getKey()));
        System.out.println("---------------------------------");
        System.out.println(MAC.encodeHmacSha256Hex(test,getKey()));
    }
}
