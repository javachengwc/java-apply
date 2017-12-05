package com.util.encrypt;

import com.util.CharsetUtil;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES是最常用的对称加密算法,密钥长度有128,192,256三种选法,JDK为它提供了PKCS5Padding的填充模式
 * JDK实现AES加密时，当密钥大于128,程序会抛出异常java.security.InvalidKeyException: Illegal key size or default parameters,
 * 表示密钥长度是受限制的，java运行时读到的是受限的policy文件。文件位于${java_home}/jre/lib/security
 * 解决此限制需在官网上下载Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files
 * 替换${java_home}/jre/lib/security/ 下面的local_policy.jar和US_export_policy.jar即可
 */
public class AES {

    public static final String KEY_ALGORITHM = "AES";//产生密钥的算法

    public static final String INNER_ALGORITHM = "AES/ECB/PKCS5Padding";//加解密算法 格式：算法/工作模式/填充模式

    //产生密钥
    public static byte[] getKey(String pwd) throws NoSuchAlgorithmException {
        KeyGenerator gen = KeyGenerator.getInstance(KEY_ALGORITHM);
        gen.init(128, new SecureRandom(pwd.getBytes()));
        SecretKey key =gen.generateKey();
        return key.getEncoded();
    }

    //还原密钥
    public static Key toKey(byte[] keyByte){
        return new SecretKeySpec(keyByte, KEY_ALGORITHM);
    }

    //AES加密
    public static byte[] encodeAES(String data, byte[] keyByte) throws Exception {
        Key key = toKey(keyByte);
        Cipher cipher = Cipher.getInstance(INNER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data.getBytes(CharsetUtil.UTF8));
    }

    //AES加密
    public static String encodeAESHex(String data, byte[] keyByte) throws Exception {
        byte[] bs = encodeAES(data, keyByte);
        //String result=  new String(bs);//乱码
        String result=Base64.encodeBase64String(bs);//转成base加密结果
        return new String(result);
    }

    //AES解密
    public static byte[] decodeAES(byte[] data, byte[] keyByte) throws Exception {
        Key key = toKey(keyByte);
        Cipher cipher = Cipher.getInstance(INNER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    //AES解密
    public static String decodeAESHex(byte[] data, byte[] keyByte) throws Exception {
        byte[] bs = decodeAES(data, keyByte);
        return new String(bs);
    }

    public static void main(String[] args) throws Exception {
        String test = "test";
        String pwd="aaa";
        byte[] keyByte = AES.getKey(pwd);
        String key=Base64.encodeBase64String(keyByte);
        System.out.println("key:"+key);
        byte[] ebyte= encodeAES(test,keyByte);
        String e=encodeAESHex(test,keyByte);
        System.out.println("encode:"+e);
        String d=decodeAESHex(ebyte, keyByte);
        System.out.println("decode:"+d);
    }
}