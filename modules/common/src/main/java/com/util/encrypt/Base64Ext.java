package com.util.encrypt;

import com.util.CharsetUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.io.*;

public class Base64Ext {

    //加密
    public static String encode(String data)  throws UnsupportedEncodingException {
//        byte[] encodedByte = Base64.encodeBase64(data.getBytes(CharsetUtil.UTF8));
//        return new String(encodedByte, CharsetUtil.UTF8);
        //上面注释部分的结果跟此结果是一样的
        return Base64.encodeBase64String(data.getBytes(CharsetUtil.UTF8));
    }

    public static String encode(byte[] bytes) throws Exception {
        return Base64.encodeBase64String(bytes);
    }

    public static String encodeByte(byte[] bytes)  throws UnsupportedEncodingException {
        return Base64.encodeBase64String(bytes);
    }

    public static String encodeFile(String filePath) throws Exception {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
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

    public static byte[] decode2Byte(String data) throws UnsupportedEncodingException{
        byte[] decodedByte = Base64.decodeBase64(data.getBytes(CharsetUtil.UTF8));
        return decodedByte;
    }

    public static byte[] decode(String base64) throws Exception {
        return Base64.decodeBase64(base64);
    }

    public static void decodeToFile(String filePath, String base64) throws Exception {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }

    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file);
                 ByteArrayOutputStream out= new ByteArrayOutputStream(2048) )
            {
                byte[] cache = new byte[1024];
                int nRead = 0;
                while ((nRead = in.read(cache)) != -1) {
                    out.write(cache, 0, nRead);
                    out.flush();
                }
                data = out.toByteArray();
            } catch (Exception e) {
                throw e;
            }
        }
        return data;
    }

    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        try {
            byte[] cache = new byte[1024];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
        }catch(Exception e) {
            throw  e;
        }finally {
            out.close();
            in.close();
        }
    }

    public static void main(String args []) throws Exception {

        String test="test";
        String cc =encode(test);
        System.out.println(cc);
        System.out.println("-------------------------");
        System.out.println(decode(cc));
    }
}
