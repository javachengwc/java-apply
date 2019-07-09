package com.micro.user.util;

import org.apache.commons.codec.binary.Hex;

public class PasswdUtil {

    public static String pwdEncrypt(String pwd, String salt) {
        String password = "";
        try {
            password = encodePassword(pwd, salt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    public static String encodePassword(String password, String salt){
        byte[] hashPwd = Digests.sha1(password.getBytes(), salt.getBytes(), 1024);
        return Hex.encodeHexString(hashPwd);
    }

}
