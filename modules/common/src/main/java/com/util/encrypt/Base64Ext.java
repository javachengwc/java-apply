package com.util.encrypt;

import com.util.CharsetUtil;
import org.apache.commons.codec.binary.Base64;
import java.io.UnsupportedEncodingException;

public class Base64Ext {

    //加密
    public static String encode(String data)  throws UnsupportedEncodingException {
//        byte[] encodedByte = Base64.encodeBase64(data.getBytes(CharsetUtil.UTF8));
//        return new String(encodedByte, CharsetUtil.UTF8);
        //上面注释部分的结果跟此结果是一样的
        return Base64.encodeBase64String(data.getBytes(CharsetUtil.UTF8));
    }

    //安全Base64加密
    public static String encodeSafe(String data) throws UnsupportedEncodingException{
        byte[] encodedByte = Base64.encodeBase64(data.getBytes(CharsetUtil.UTF8), true);
        return new String(encodedByte, CharsetUtil.UTF8);
    }

    //Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548)
    public static String base64UrlSafeEncode(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    //解密
    public static String decode(String data) throws UnsupportedEncodingException{
        byte[] decodedByte = Base64.decodeBase64(data.getBytes(CharsetUtil.UTF8));
        return new String(decodedByte, CharsetUtil.UTF8);
    }

    public static void main(String args []) throws Exception {

        String test="test";
        String cc =encode(test);
        System.out.println(cc);
        System.out.println("-------------------------");
        System.out.println(decode(cc));
    }
}
