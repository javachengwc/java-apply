package com.util.encrypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

import com.util.CharsetUtil;
import org.apache.commons.codec.binary.Base64;

/**
 * RSA是非常经典的非对称加密算法
 * 加解密过程:
 * a,发送方构建密钥对，自己保留私钥，将公钥发送给接收方
 * b,发送方使用密钥对消息进行加密，接收方使用公钥对消息解密（“私钥加密，公钥解密”）
 *   或者接收方使用公钥对消息进行加密，发送方使用私钥对消息解密（“公钥加密，私钥解密”）
 */
public class RSA {

    private static final String KEY_ALGORITHM = "RSA";//密钥算法

    private static final String INNER_ALGORITHM = "RSA/ECB/PKCS1Padding";//加解密算法 格式：算法/工作模式/填充模式

    private static final int KEY_SIZE = 512;//密钥长度

    //生成密钥对
    public static KeyPair genKey() throws NoSuchAlgorithmException{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    //公钥
    public static byte[] getPublicKey(KeyPair keyPair){
        return keyPair.getPublic().getEncoded();
    }

    //私钥
    public static byte[] getPrivateKey(KeyPair keyPair){
        return keyPair.getPrivate().getEncoded();
    }

    //还原公钥
    public static PublicKey toPublicKey(byte[] pubKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);//密钥工厂
        return keyFactory.generatePublic(new X509EncodedKeySpec(pubKey));//还原公钥
    }

    //还原私钥
    public static PrivateKey toPrivateKey(byte[] priKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(priKey));
    }

    //私钥加密
    public static byte[] encodeByPriKey(String data, byte[] keyByte) throws Exception {
        PrivateKey priKey = toPrivateKey(keyByte);
        Cipher cipher = Cipher.getInstance(INNER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        return cipher.doFinal(data.getBytes(CharsetUtil.UTF8));
    }

    //公钥加密
    public static byte[] encodeByPubKey(String data, byte[] keyByte) throws Exception {
        PublicKey pubKey = toPublicKey(keyByte);
        Cipher cipher = Cipher.getInstance(INNER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data.getBytes(CharsetUtil.UTF8));
    }

    //私钥解密
    public static byte[] decodeByPriKey(byte[] data, byte[] keyByte) throws Exception {
        PrivateKey priKey = toPrivateKey(keyByte);
        Cipher cipher = Cipher.getInstance(INNER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return cipher.doFinal(data);
    }

    //公钥解密
    public static byte[] decodeByPubKey(byte[] data, byte[] keyByte) throws Exception {
        PublicKey pubKey = toPublicKey(keyByte);//还原公钥
        Cipher cipher = Cipher.getInstance(INNER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception{
        KeyPair keyPair = RSA.genKey();//生成密钥对
        byte[] pubKey = RSA.getPublicKey(keyPair);
        byte[] priKey = RSA.getPrivateKey(keyPair);
        System.out.println("pubKey:"+Base64.encodeBase64String(pubKey));
        System.out.println("priKey:"+Base64.encodeBase64String(priKey));

        System.out.println("甲方-->乙方");
        String data = "test";
        byte[] encodeStr = RSA.encodeByPriKey(data, priKey);
        System.out.println("甲方加密后数据:"+Base64.encodeBase64String(encodeStr));
        byte[] decodeStr = RSA.decodeByPubKey(encodeStr, pubKey);
        System.out.println("乙方解密后数据:"+new String(decodeStr,CharsetUtil.UTF8));
        System.out.println("乙方-->甲方");
        String data1 = "ok";
        byte[] encodeStr1 = RSA.encodeByPubKey(data1, pubKey);
        System.out.println("乙方加密后数据:"+Base64.encodeBase64String(encodeStr1));
        byte[] decodeStr1 = RSA.decodeByPriKey(encodeStr1, priKey);
        System.out.println("甲方解密后数据:"+new String(decodeStr1,CharsetUtil.UTF8));
    }
}