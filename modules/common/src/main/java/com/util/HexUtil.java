package com.util;

/**
 * hex码(16进制)工具类
 */
public class HexUtil {

    final static int LINE_COUNT = 16;

    final static int WORD_COUNT = 2;

    public static StringBuffer toHex(byte b) {
        byte factor = 16;
        int v = b & 0xff;// 去掉byte转换之后的负数部分。
        byte high = (byte) (v / factor);
        byte low = (byte) (v % factor);
        StringBuffer buf = new StringBuffer();
        buf.append(toHexLow(high)).append(toHexLow(low));
        return buf;
    }

    private static char toHexLow(byte b) {
        if (b > 16 || b < 0) {
            throw new IllegalArgumentException(
                    "inpt parameter should less than 16 and greater than 0");
        }
        if (b < 10) {
            return (char) ('0' + (char) b);
        } else {
            return (char) ('A' + (b - 10));

        }
    }

    //十进制转十六进制
    public static StringBuffer toHex(int val) {
        StringBuffer buf = toHex((byte) (val >> 24 & 0xff)).append(
                toHex((byte) (val >> 16 & 0xff)));
        return buf.append(toHex((byte) (val >> 8 & 0xff))).append(
                toHex((byte) (val & 0xff)));
    }

    //二进制转十六进制
    public static String binary2Hex(String bString) {
        if (bString == null || bString.equals("")) {
            return null;
        }
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    //二进制转十六进制
    public static String binary2Hex(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    //十六进制转二进制
    public static String hex2Binary(String hexString) {
        if (hexString == null ) {
            return null;
        }
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    //十六进制转二进制
    public static byte[] hex2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    //打印二进制数组
    public static void printBytes(byte[] arr, int off, int len) {
        if (arr == null || len <= 0 || off < 0 || off + len > arr.length) {
            return;
        }

        int count = 0;

        for (int i = off; count < len; ++i) {
            System.out.print(toHex(arr[i]));

            ++count;
            if (count % WORD_COUNT == 0) {
                System.out.print(' ');
            }
            if (count % LINE_COUNT == 0) {
                System.out.println();
            }
        }
    }

    public static void printBytes(byte[] arr) {
        printBytes(arr,0,arr.length);
    }

    //字符串转二进制
    public static String strToBinstr(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]) + " ";
        }
        return result;
    }

    public static void main(String[] args) {

        String aaa="BD";
        System.out.println(strToBinstr(aaa));

        System.out.println("十六进制转二进制"+hex2Binary("B"));
        System.out.println("二进制转十六进制"+binary2Hex("1010"));

        int a22=0b1011;//0b开头表示二进制,0开头是八进制，默认是十进制，0x或0X开头是十六进制
        System.out.println(a22);

        byte[] arr = new byte[256];
        for (int i = 0; i < 256; ++i) {
            arr[i] = (byte) i;
        }

        printBytes(arr, 0, 256);
        System.out.println("-------------------");
        printBytes(arr, 240, 16);

        System.out.println(toHex(1));
        System.out.println(toHex((byte)1));
        System.out.println(toHex(2));
        System.out.println(toHex((byte)2));
        System.out.println(toHex(0xffffffff));
        System.out.println(toHex(0xeeffaacc));
    }
}
