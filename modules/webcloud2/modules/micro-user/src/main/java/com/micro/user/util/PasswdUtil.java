package com.micro.user.util;

import org.apache.commons.codec.binary.Hex;

public class PasswdUtil {

    public static PasswdPair passwdEncrypt(String pwd) {
        PasswdPair passwdPair = new PasswdPair();
        try {
            byte[] salts = Digests.generateSalt(5);
            String salt = Hex.encodeHexString(salts);
            String passwd = encodePasswd(pwd, salt);
            passwdPair.setPasswd(passwd);
            passwdPair.setSalt(salt);
            return passwdPair;
        } catch (Exception e) {
            e.printStackTrace();
            return passwdPair;
        }
    }

    public static String passwdEncrypt(String pwd, String salt) {
        String passwd = "";
        try {
            passwd = encodePasswd(pwd, salt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return passwd;
    }

    public static String encodePasswd(String passwd, String salt){
        byte[] hashPwd = Digests.sha1(passwd.getBytes(), salt.getBytes(), 1024);
        return Hex.encodeHexString(hashPwd);
    }

    public static class PasswdPair {

        private String passwd;

        private String salt;

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }
    }
}
