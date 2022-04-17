package com.httpclient.openapi.utils;

import java.security.MessageDigest;

public class Encoder {
    private static final String ALGORITHM = "MD5";
    private static final String CHARSET = "UTF-8";
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Encoder() {
    }

    public static String encode(String algorithm, String str) {
        if (str == null) {
            return null;
        } else {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
                messageDigest.update(str.getBytes("UTF-8"));
                String encodeString = getFormattedText(messageDigest.digest());
                return encodeString;
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        }
    }

    public static String encodeByMD5(String str) {
        return str == null ? null : encode("MD5", str);
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);

        for(int j = 0; j < len; ++j) {
            buf.append(HEX_DIGITS[bytes[j] >> 4 & 15]);
            buf.append(HEX_DIGITS[bytes[j] & 15]);
        }

        return buf.toString();
    }
}

