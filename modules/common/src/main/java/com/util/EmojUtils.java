package com.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojUtils {

    private static Pattern patternEmoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[`~!@#～¥$%^&()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&（）——+|{}【】‘；：”“’。，、？]");

    public static String filterEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return source;
        }
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            }
        }
        if (buf == null) {
            return "";
        } else {
            if (buf.length() == len) {
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    public static String replaceEmoji(String msg) {
        String value = StringUtils.replace(msg,"️","");
        if (StringUtils.isBlank(StringUtils.trim(msg))) {
            return value;
        }
        String reStr = "";
        Matcher emojiMatcher = patternEmoji.matcher(value);
        if(emojiMatcher.find()){
            value = emojiMatcher.replaceAll(reStr);
        }
        return value;
    }

    public static void main(String [] args ) {
        String info ="直\uD83E\uDDDA\uD83C\uDFFC\u200D♀️播";
        System.out.println(info);
        System.out.println(replaceEmoji(info));
        System.out.println(filterEmoji(info));
        String rt = filterEmoji(replaceEmoji(info));
        System.out.println(rt);
    }
}
