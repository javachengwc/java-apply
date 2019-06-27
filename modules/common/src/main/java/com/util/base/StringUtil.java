package com.util.base;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");

    /**
     * 判断字符串是否相等
     */
    public static boolean strEquals(String a,String b)
    {
        if(a==null && b!=null)
        {
            return false;
        }
        if(a!=null && !a.equals(b))
        {
            return false;
        }
        return true;
    }

    /**
     * 判断是否纯字母组合
     */
    public static boolean isAbc(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = abcPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    /**
     * 将字符串的首字符转成大写
     */
    public static String capitalize(String str) {
        int strLen;

        if ((str == null) || ((strLen = str.length()) == 0)) {
            return str;
        }

        return new StringBuffer(strLen).append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    /**
     * 将字符串的首字符转成小写
     */
    public static String initialLower(String str) {
        int strLen;

        if ((str == null) || ((strLen = str.length()) == 0)) {
            return str;
        }

        return new StringBuffer(strLen).append(Character.toLowerCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }


    /**
     * 将小写转换为大写
     */
    public static char toUpperCase(char c){
        return (char)(c -  ('a' - 'A'));
    }

    public static String getHexString(String source){
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<source.length();i++){
            buffer.append("\\u"+Integer.toHexString(source.charAt(i)));
        }
        return buffer.toString();
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     */
    public static String joinString(List<String> array, String symbol) {
        StringBuffer buf = new StringBuffer();
        String result =null;
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                String temp = array.get(i);
                if (temp != null && temp.trim().length() > 0) {
                    buf.append(temp).append(symbol);
                }
            }
            result = buf.toString();
            if (result.length() > 1) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

    /**
     * 随机创建常用中文字符
     * @param count 长度
     */
    public static String randomCommonChinese(int count) {
        Object[] os = createCommonRegionCode(count);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < os.length; ++i) {
            try {
                byte[] bs = (byte[]) os[i];
                buffer.append(new String(bs, "gb2312"));
            } catch (Throwable e) {
                buffer.append(" ");
            }
        }
        return buffer.toString();
    }

    /**
     * 随机创建中文字符
     * @param count 长度
     * @return
     */
    public static String randomChinese(int count) {
        Object[] os = createRegionCode(count);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < os.length; ++i) {
            try {
                byte[] bs = (byte[]) os[i];
                buffer.append(new String(bs, "gb2312"));
            } catch (Throwable e) {
                buffer.append(" ");
            }
        }
        return buffer.toString();
    }

    /**
     * 原理：根据汉字的编码原理随机产生字节码，在组装成汉字。
     * 规则：第15区也就是AF区以前都没有汉字，只有少量符号， 汉字都从第16区B0开始，并且从区位D7开始以后的汉字都
     * 是和很难见到的繁杂汉字，所以这些都要排出掉。所以随 机生成的汉字十六进制区位码第1位范围在B、C、D之间，
     * 如果第1位是D的话，第2位区位码就不能是7以后的十六进制 数。在来看看区位码表发现每区的第一个位置和最后一个位
     * 置都是空的，没有汉字，因此随机生成的区位码第3位如果是 A的话，第4位就不能是0；第3位如果是F的话，第4位就不能是F。
     *
     * @param strlength
     * @return
     */
    private static Object[] createCommonRegionCode(int strlength) {
        // 定义一个字符串数组储存汉字编码的组成元素
        String[] full_range = new String[] { "0", "1", "2", "3", "4", "5", "6",
                "7", "8", "9", "a", "b", "c", "d", "e", "f" };

        // 定义一个object数组用来
        Object[] bytes = new Object[strlength];

        for (int i = 0; i < strlength; i++) {
            // 区位码第1位
            int d1 = (int) (Math.random() * 3) + 11;
            String str1 = full_range[d1];

            // 区位码第2位
            // 种子避免产生重复值
            int d2;
            String str2 = null;
            if (d1 == 13) {
                d2 = (int) (Math.random() * 8);
                str2 = full_range[d2];
            } else {
                d2 = (int) (Math.random() * 16);
                str2 = full_range[d2];
            }

            // 区位码第3位
            int d3 = (int) (Math.random() * 6) + 10;
            String str3 = full_range[d3];

            // 区位码第4位
            int d4;
            if (d3 == 10) {
                d4 = (int) (Math.random() * 15) + 1;
            } else if (d3 == 15) {
                d4 = (int) (Math.random() * 15);
            } else {
                d4 = (int) (Math.random() * 16);
            }
            String str4 = full_range[d4];

            // 定义两个字节变量存储产生的随机汉字区位码
            byte b1 = Integer.decode("0x" + str1 + str2).byteValue();
            byte b2 = Integer.decode("0x" + str3 + str4).byteValue();
            byte[] bs = { b1, b2 };

            // 将产生的一个汉字的字节数组放入object数组中
            bytes[i] = bs;

        }

        return bytes;

    }

    /**
     * 原理：根据汉字的编码原理随机产生字节码，在组装成汉字。
     * 规则：第15区也就是AF区以前都没有汉字，只有少量符号， 汉字都从第16区B0开始，并且从区位F8开始以后没有汉字，
     * 所以这些都要排出掉。在来看看区位码表发现每区的第一个位置和最后一个位 置都是空的，没有汉字，因此随机生成的区位码第3位如果是
     * A的话，第4位就不能是0；第3位如果是F的话，第4位就不能是F。
     *
     * @param strlength
     * @return
     */
    private static Object[] createRegionCode(int strlength) {
        // 定义一个字符串数组储存汉字编码的组成元素
        String[] full_range = new String[] { "0", "1", "2", "3", "4", "5", "6",
                "7", "8", "9", "a", "b", "c", "d", "e", "f" };

        // 定义一个object数组用来
        Object[] bytes = new Object[strlength];

        for (int i = 0; i < strlength; i++) {
            // 区位码第1位
            int d1 = (int) (Math.random() * 5) + 11;
            String str1 = full_range[d1];

            // 区位码第2位
            // 种子避免产生重复值
            int d2;
            String str2 = null;
            if (d1 == 15) {
                d2 = (int) (Math.random() * 8);
                str2 = full_range[d2];
            } else {
                d2 = (int) (Math.random() * 16);
                str2 = full_range[d2];
            }

            // 区位码第3位
            int d3 = (int) (Math.random() * 6) + 10;
            String str3 = full_range[d3];

            // 区位码第4位
            int d4;
            if (d3 == 10) {
                d4 = (int) (Math.random() * 15) + 1;
            } else if (d3 == 15) {
                d4 = (int) (Math.random() * 15);
            } else {
                d4 = (int) (Math.random() * 16);
            }
            String str4 = full_range[d4];

            // 定义两个字节变量存储产生的随机汉字区位码
            byte b1 = Integer.decode("0x" + str1 + str2).byteValue();
            byte b2 = Integer.decode("0x" + str3 + str4).byteValue();
            byte[] bs = { b1, b2 };

            // 将产生的一个汉字的字节数组放入object数组中
            bytes[i] = bs;

        }

        return bytes;

    }

    public static byte[] getBytes(InputStream is) throws Exception {
        byte[] data = (byte[]) null;
        Collection chunks = new ArrayList();
        byte[] buffer = new byte[1024000];
        int read = -1;
        int size = 0;

        while ((read = is.read(buffer)) != -1) {
            if (read > 0) {
                byte[] chunk = new byte[read];
                System.arraycopy(buffer, 0, chunk, 0, read);
                chunks.add(chunk);
                size += chunk.length;
            }
        }

        if (size > 0) {
            ByteArrayOutputStream bos = null;
            try {
                bos = new ByteArrayOutputStream(size);
                for (Iterator itr = chunks.iterator(); itr.hasNext();) {
                    byte[] chunk = (byte[]) itr.next();
                    bos.write(chunk);
                }
                data = bos.toByteArray();
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        }
        return data;
    }

    /**
     * HTML转码
     * @param value
     * @return HTMLEncode
     */
    public static String encode4Html(String value) {
        if(StringUtils.isBlank(value))
        {
            return "";
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
			/*
			 * if (ch == '<') { result.append("&lt;"); } else
			 */if (ch == '&') {
                result.append("&amp;");
            } else if (ch == '"') {
                result.append("&quot;");
            } /*
			 * else if (ch == '\r') { result.append("<BR>"); }
			 */else if (ch == '\n') {
                if (value.charAt(i - 1) == '\r') {

                } else {
                    result.append("<BR>");
                }
            } /*
			 * else if (ch == '\t') { result.append("&nbsp;&nbsp;&nbsp;&nbsp");
			 * }
			 *//*
				 * else if (ch == ' ') { result.append("&nbsp;"); }
				 */else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static boolean hasChinese(String str)
    {
        if(StringUtils.isBlank(str))
        {
            return false;
        }
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++)
        {
            if (isChinese(charArray[i]))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isChinese(char str)
    {
        if ((str >= 0x4e00) && (str <= 0x9fbb))
        {
            return true;
        }
        return false;
    }

    /**
     * 判断字符是否是中文编码
     */
    public static boolean isChinese2(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * field名称转换成表列名
     * @param fieldName
     */
    public static String  field2Col(String fieldName)
    {

        if(StringUtils.isBlank(fieldName))
        {
            return "";
        }
        int len = fieldName.length();
        StringBuffer buf = new StringBuffer();
        for(int i=0;i<len;i++)
        {
            char c= fieldName.charAt(i);
            if(c>='A'&& c<='Z'&& i>0)
            {
                buf.append("_");
                c+=32;
            }
            buf.append(c);
        }
        return buf.toString();
    }

    /**
     * 表列名转换成field名称
     * @param columnName
     * @return
     */
    public static String col2Filed(String columnName)
    {

        if(StringUtils.isBlank(columnName))
        {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        String ps []= columnName.split("_");
        int len = ps.length;
        if(len==1)
        {
            return columnName.toLowerCase();
        }
        for(int i=0;i<len;i++)
        {
            if(i==0)
            {
                buf.append(ps[i]);
            }
            else
            {
                buf.append(ps[i].substring(0, 1).toUpperCase() + ps[i].substring(1));
            }
        }
        return buf.toString();
    }

    /**
     * 对sql参数进行过滤,避免sql注入
     */
    public static String escapeSql(String sqlParam) {
        sqlParam = StringEscapeUtils.escapeSql(sqlParam);
        return sqlParam;
    }

    /**
     * 过滤javascript字符,避免脚本注入
     */
    public static String escapeJavaScript(String input) {
        return StringEscapeUtils.escapeJavaScript(input);
    }

    /**
     * 高效替换字符串的方法
     * 可替换特殊字符的替换方法,replaceAll只能替换普通字符串,含有特殊字符的不能替换
     */
    public static String replaceStr(String strSource, String strFrom,
                                    String strTo) {
        if (strSource == null) {
            return null;
        }
        try {
            int i = 0;
            if ((i = strSource.indexOf(strFrom, i)) >= 0) {
                char[] cSrc = strSource.toCharArray();
                char[] cTo = strTo.toCharArray();
                int len = strFrom.length();
                StringBuilder sb = new StringBuilder();
                sb.append(cSrc, 0, i).append(cTo);
                i += len;
                int j = i;
                while ((i = strSource.indexOf(strFrom, i)) > 0) {
                    sb.append(cSrc, j, i - j).append(cTo);
                    i += len;
                    j = i;
                }
                sb.append(cSrc, j, cSrc.length - j);
                strSource = sb.toString();
                sb = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            strSource = "";
        }
        return strSource;
    }

    /**
     * 字符串编码转换
     */
    public static String convertEncode(String rawStr, String characterSet) {
        String str = null;
        try {
            str = new String(rawStr.getBytes(), characterSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * 转换编码
     * @param s 源字符串
     * @param fencode 源编码格式
     * @param bencode 目标编码格式
     */
    public static String changEncode(String s, String fencode, String bencode) {
        String str=null;
        try {
            if (!StringUtils.isBlank(s)) {
                str = new String(s.getBytes(fencode), bencode);
            } else {
                str = "";
            }
            return str;
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * 去除字符串中的空格,回车,换行符,制表符
     */
    public static String replaceBlank(String strSource) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(strSource);
        String afterStr = m.replaceAll("");
        return afterStr;
    }

    /**
     * 全角字符变半角字符
     */
    public static String full2Half(String str) {
        if (str == null || "".equals(str))
            return "";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c >= 65281 && c < 65373)
                sb.append((char) (c - 65248));
            else
                sb.append(str.charAt(i));
        }

        return sb.toString();

    }

    public static String html2Text(String inputString) {
        String htmlStr = inputString; //含html标签的字符串
        String textStr ="";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签

            textStr = htmlStr;

        }catch(Exception e) {
            // System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;//返回文本字符串
    }

    public static String escapeHTML(String s) {
        StringBuffer sb = new StringBuffer();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case 'à':
                    sb.append("&agrave;");
                    break;
                case 'À':
                    sb.append("&Agrave;");
                    break;
                case 'â':
                    sb.append("&acirc;");
                    break;
                case 'Â':
                    sb.append("&Acirc;");
                    break;
                case 'ä':
                    sb.append("&auml;");
                    break;
                case 'Ä':
                    sb.append("&Auml;");
                    break;
                case 'å':
                    sb.append("&aring;");
                    break;
                case 'Å':
                    sb.append("&Aring;");
                    break;
                case 'æ':
                    sb.append("&aelig;");
                    break;
                case 'Æ':
                    sb.append("&AElig;");
                    break;
                case 'ç':
                    sb.append("&ccedil;");
                    break;
                case 'Ç':
                    sb.append("&Ccedil;");
                    break;
                case 'é':
                    sb.append("&eacute;");
                    break;
                case 'É':
                    sb.append("&Eacute;");
                    break;
                case 'è':
                    sb.append("&egrave;");
                    break;
                case 'È':
                    sb.append("&Egrave;");
                    break;
                case 'ê':
                    sb.append("&ecirc;");
                    break;
                case 'Ê':
                    sb.append("&Ecirc;");
                    break;
                case 'ë':
                    sb.append("&euml;");
                    break;
                case 'Ë':
                    sb.append("&Euml;");
                    break;
                case 'ï':
                    sb.append("&iuml;");
                    break;
                case 'Ï':
                    sb.append("&Iuml;");
                    break;
                case 'ô':
                    sb.append("&ocirc;");
                    break;
                case 'Ô':
                    sb.append("&Ocirc;");
                    break;
                case 'ö':
                    sb.append("&ouml;");
                    break;
                case 'Ö':
                    sb.append("&Ouml;");
                    break;
                case 'ø':
                    sb.append("&oslash;");
                    break;
                case 'Ø':
                    sb.append("&Oslash;");
                    break;
                case 'ß':
                    sb.append("&szlig;");
                    break;
                case 'ù':
                    sb.append("&ugrave;");
                    break;
                case 'Ù':
                    sb.append("&Ugrave;");
                    break;
                case 'û':
                    sb.append("&ucirc;");
                    break;
                case 'Û':
                    sb.append("&Ucirc;");
                    break;
                case 'ü':
                    sb.append("&uuml;");
                    break;
                case 'Ü':
                    sb.append("&Uuml;");
                    break;
                case '®':
                    sb.append("&reg;");
                    break;
                case '©':
                    sb.append("&copy;");
                    break;
                case '€':
                    sb.append("&euro;");
                    break;
                case ' ':
                    sb.append("&nbsp;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    //转换成1行
    public static String str2OneLine(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());

        for (char c : str.toCharArray()) {
            if (c == '\t' || c == '\n' || c == '\r') {
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    //去掉指定前缀
    public static String removePrefix(String str, String prefix) {
        if (str==null || StringUtils.isBlank(str) || prefix==null || StringUtils.isBlank(prefix)) {
            return str;
        }
        if (str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return str;
    }

    //去掉指定后缀
    public static String removeSuffix(String str, String suffix) {
        if (str==null || StringUtils.isBlank(str) || suffix==null || StringUtils.isBlank(suffix)) {
            return str;
        }
        if (str.endsWith(suffix)) {
            return str.substring(0, str.length() - suffix.length());
        }
        return str;
    }

    /**
     * @param str    String
     * @param fromIndex 开始的index（包括）
     * @param toIndex   结束的index（不包括）
     * @return 字串
     */
    public static String subStr(String str, int fromIndex, int toIndex) {
        int len = str.length();
        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex >= len) {
            fromIndex = len - 1;
        }
        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }
        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }
        if (fromIndex == toIndex) {
            return "";
        }
        char[] strArray = str.toCharArray();
        char[] newStrArray = Arrays.copyOfRange(strArray, fromIndex, toIndex);
        return new String(newStrArray);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 例如：format("aaa {} ccc", "bbb")   ---->    aaa bbb ccc
     */
    public static String format(String template, Object... values) {
        if (values==null || values.length<=0 || StringUtils.isBlank(template)) {
            return template;
        }

        final StringBuilder sb = new StringBuilder();
        final int length = template.length();

        int valueIndex = 0;
        char currentChar;
        for (int i = 0; i < length; i++) {
            if (valueIndex >= values.length) {
                sb.append(subStr(template, i, length));
                break;
            }

            currentChar = template.charAt(i);
            if (currentChar == '{') {
                final char nextChar = template.charAt(++i);
                if (nextChar == '}') {
                    sb.append(values[valueIndex++]);
                } else {
                    sb.append('{').append(nextChar);
                }
            } else {
                sb.append(currentChar);
            }

        }

        return sb.toString();
    }

    /**
     * 格式化文本，使用 {varName} 占位<br>
     * map = {a: "aa", b: "bb"}
     * format("{a} and {b}", map)    ---->    aa and bb
     */
    public static String format(String template, Map<?, ?> map) {
        if (null == map || map.isEmpty()) {
            return template;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return template;
    }

    public static void main(String args [])
    {
        System.out.println(randomCommonChinese(3)+","+randomChinese(3));
        String fName ="is_hotWhat";
        System.out.println(field2Col(fName));

        String col="fehi_shid_ahagf";
        System.out.println(col2Filed(col));
        System.out.println(full2Half("．"));
    }

}
