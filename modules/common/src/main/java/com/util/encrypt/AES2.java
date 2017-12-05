package com.util.encrypt;

import com.util.CharsetUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密算法，与js的aes算法加解密匹配
 */
public class AES2 {

    public static byte[] aesEncode2Bytes(String data, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES.KEY_ALGORITHM);
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(AES.INNER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(CharsetUtil.UTF8), AES.KEY_ALGORITHM));
        return cipher.doFinal(data.getBytes(CharsetUtil.UTF8));
    }

    //加密
    public static String aesEncode(String data, String key) throws Exception {
        return Base64Ext.encodeByte(aesEncode2Bytes(data, key));
    }

    public static String aesDecodeByBytes(byte[] bytes, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES.KEY_ALGORITHM);
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(AES.INNER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(CharsetUtil.UTF8), AES.KEY_ALGORITHM));
        byte[] decryptBytes = cipher.doFinal(bytes);
        return new String(decryptBytes);
    }

    //解密
    public static String aesDecode(String data, String key) throws Exception {
        return aesDecodeByBytes(Base64Ext.decode2Byte(data), key);
    }

    public static void main(String[] args) throws Exception {
        String key = "aabbccddeeffgghh";
        String data = "test";
        String encrypt = aesEncode(data, key);
        System.out.println("加密后：" + encrypt);
        String decrypt = aesDecode(encrypt,key);
        System.out.println("解密后：" + decrypt);
    }
}
