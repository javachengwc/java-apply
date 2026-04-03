package com.util;

import java.text.BreakIterator;

public class EmojUtils {

    private static boolean isEmojiBase(int cp) {
        return (cp >= 0x2600 && cp <= 0x27BF) || // 杂项符号
                (cp == 0x2B50) || (cp == 0x2B55) || // 星号
                (cp >= 0x231A && cp <= 0x231B) || // 手表/沙漏
                (cp >= 0x23E9 && cp <= 0x23F3) || // 媒体控制
                (cp >= 0x23F8 && cp <= 0x23FA) ||
                (cp >= 0x25AA && cp <= 0x25AB) ||
                (cp == 0x25B6) || (cp == 0x25C0) ||
                (cp >= 0x25FB && cp <= 0x25FE) ||
                (cp >= 0x1F000 && cp <= 0x1F9FF) || // Emoji 核心区块
                (cp >= 0x1FA00 && cp <= 0x1FA6F) || // 扩展 A
                (cp >= 0x1FA70 && cp <= 0x1FAFF) || // 扩展 B
                (cp >= 0xE0020 && cp <= 0xE007F);    // 标签符
    }

    /**
     * 是否包含表情符
     */
    public static boolean containsEmoji(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(text);

        for (int s = it.first(), e = it.next(); e != BreakIterator.DONE; s = e, e = it.next()) {
            int firstCp = text.codePointAt(s);
            if (isEmojiBase(firstCp)) {
                return true; // 发现 Emoji 立即返回，避免无效遍历
            }
        }
        return false;
    }

    /**
     * 移除表情符
     */
    public static String removeEmoji(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(text);
        StringBuilder sb = new StringBuilder(text.length());

        // 按字素簇迭代（🧚🏼‍♀️ 会被识别为 1 个簇）
        for (int s = it.first(), e = it.next(); e != BreakIterator.DONE; s = e, e = it.next()) {
            // 获取该簇的第一个码点（Emoji 组合序列必定以基础 Emoji 开头）
            int firstCp = text.codePointAt(s);
            // 非 Emoji 簇才保留
            if (!isEmojiBase(firstCp)) {
                sb.append(text, s, e);
            }
        }
        return sb.toString();
    }


    public static void main(String [] args ) {
        String info ="直\uD83E\uDDDA\uD83C\uDFFC\u200D♀️播";
        info="直播2332♀️";
        System.out.println(info);
        System.out.println(containsEmoji(info));
        System.out.println(removeEmoji(info));
    }
}
