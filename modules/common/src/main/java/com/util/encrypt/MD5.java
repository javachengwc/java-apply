package com.util.encrypt;

import com.util.CharsetUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

/**
 * MD5数字签名（已破解）,单向加密（即只有加密，没有解密）
 */
public class MD5
{
    public static void main (String args[]) throws Exception
    {
        String str = "test";
        System.out.println(getMD5(str));
        System.out.println("-------------------------------");
        System.out.println(encodeMd5Hex(str));
    }

    public static String getMD5(String source)
    {
        return getMD5(source.getBytes());
    }

    public static String getMD5(byte source[])
    {
        String s = null;
        char hexDigits[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
            'a', 'b', 'c', 'd', 'e', 'f'
        };
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char str[] = new char[32];
            int k = 0;
            for(int i = 0; i < 16; i++)
            {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            s = new String(str);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return s;
    }

    //使用commons codec的md5加密
    public static byte[] encode(String data) throws Exception {
        return DigestUtils.md5(data.getBytes(CharsetUtil.UTF8));
    }

    public static String encodeMd5Hex(String data) throws Exception {
        //直接使用new String(encodedByte,"UTF-8")不行
        return new String(DigestUtils.md5Hex(data.getBytes(CharsetUtil.UTF8)));
    }
}

