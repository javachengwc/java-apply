package com.util.encrypt;

import java.security.MessageDigest;

/**
 * MD5数字签名算法
 */
public class MD5
{
	public MD5()
    {
		
    }

    public static void main(String args[])
    {
        String str = "abc";
        System.out.println(getMD5(str));
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
}

