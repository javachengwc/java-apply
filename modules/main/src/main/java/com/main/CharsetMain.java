package com.main;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * 原java文件是什么编码Charset.defaultCharset()就是什么编码
 * 也就是说 如果CharsetTest.java是GBK编码，Charset.defaultCharset()就是GBK
 * 如果CharsetTest.java是UTF-8编码，Charset.defaultCharset()就是UTF-8
 */
public class CharsetMain {
	
	public static void main(String args []) throws Exception
	{
		System.out.println(Charset.defaultCharset());

        testURLDecoder();
    	
	}

    public static void testURLDecoder() throws Exception
    {
        String tt = "%E5%A4%A9%E5%A4%A9%E7%89%B9%E4%BB%B7";

        System.out.println(URLDecoder.decode(tt, "utf-8"));
    }

}
