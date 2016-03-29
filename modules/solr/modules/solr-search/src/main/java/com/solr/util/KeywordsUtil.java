package com.solr.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordsUtil {

    public static String transformSolrMetacharactor(String input){
        StringBuffer sb = new StringBuffer();
        String regex = "[+/\\-&|!(){}\\[\\]^\"~*?:(\\)<>]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            matcher.appendReplacement(sb, "\\\\\\\\"+matcher.group());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
