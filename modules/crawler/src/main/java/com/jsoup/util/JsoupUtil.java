package com.jsoup.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * jsoup工具类
 */
public class JsoupUtil {

    public static Document getDocument(String url) throws IOException {

        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .timeout(60000).ignoreContentType(true).get();
    }

    public static String getText(String url) throws IOException {
        Document doc = getDocument(url);
        return doc.text();
    }
}
