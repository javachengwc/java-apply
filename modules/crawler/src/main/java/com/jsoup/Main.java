package com.jsoup;

import com.jsoup.util.JsoupUtil;

/**
 *
 */
public class Main {

    public static void main(String args []) throws Exception
    {
        System.out.println(JsoupUtil.getText("http://www.baidu.com"));
    }
}
