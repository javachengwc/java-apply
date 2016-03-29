package com.captcha.cage;

import com.github.cage.IGenerator;

import java.util.Random;

/**
 *
 */
public class GageTokenGenerator implements IGenerator<String> {
    private int length = 4;
    private String charsetdir = "23456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKLMNPQRSTUVWXYZ";
    private static final Random r = new Random();

    public GageTokenGenerator() {

    }
    public GageTokenGenerator(int length, String charsetdir) {
        this.length = length;
        this.charsetdir = charsetdir;
    }

    @Override
    public String next() {
        StringBuffer sb = new StringBuffer();
        int len = charsetdir.length();
        for (int i = 0; i < length; i++) {
            sb.append(charsetdir.charAt(r.nextInt(len - 1)));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        GageTokenGenerator t = new GageTokenGenerator();
        for (int i = 0; i < 100; i++) {
            System.out.println(t.next());
        }
    }
}