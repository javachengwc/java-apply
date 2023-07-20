package com.tmp.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static final Logger LOGGER = Logger.getLogger(MD5Util.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static String encryptMD5(String s) {

        MessageDigest md5 = getMessageDigest("MD5");
        md5.update(getContentBytes(s, DEFAULT_CHARSET));
        return new String(Base64.encodeBase64(md5.digest()));
    }

    public static String encryptMD5String(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            if(s == null){
                return null;
            }
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest   mdInst = getMessageDigest("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOGGER.error(e);
            return "";
        }
    }

    public static byte[] getContentBytes(String content, String charset) {
        if(content == null){
            return null;
        }
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
                    + charset);
        }
    }

    public static String getSign(String s, String token) {
        s = s + token;
        String ret = "";
        try {
            ret = MD5Util.encryptMD5(s);
        }
        catch (Exception e) {
            LOGGER.error(e);
        }
        return ret.trim();
    }

    protected static MessageDigest getMessageDigest(String model) {
        MessageDigest md5 =null;
        try {
            md5 = MessageDigest.getInstance(model);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e);
        }
        return md5;
    }
}
