package com.other.gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

public class Colorful {

    private final int LENGTH = 20;
    private int x;
    private int speed;
    private int[] ys;
    private String[] num;
    Random r = new Random();

    public Colorful() {

    }
    public Colorful(int x, int speed) {
        this.x = x;
        this.speed = speed;
        ys = new int[Hack.HEIGHT / LENGTH];
        num = new String[Hack.HEIGHT / LENGTH];
        for (int i = 0; i < ys.length; i++) {
            ys[i] = -(i * LENGTH);
        }
        for (int i = 0; i < num.length; i++) {
            String str = r.nextInt(2) + "";
            num[i] = str;
        }
    }
    public void draw(Graphics g) {
        g.setFont(new Font("宋体", Font.BOLD, 15));
        g.setColor(Color.white);
        /*
         * Color c=new Color(r.nextInt(256),r.nextInt(256),r.nextInt(256));
         * g.setColor(c);
         */// 可以将数字变成彩色,不建议
        for (int i = 0; i < ys.length; i++) {
            if (ys[i] < 0) {
                continue;
            }
            g.drawString(num[i], x, ys[i]);
        }
        move();
    }
    private void move() {
        for (int i = 0; i < ys.length; i++) {
            ys[i] += speed;
            if (ys[i] > Hack.HEIGHT) {
                ys[i] = 0;
                num[i] = r.nextInt(2) + "";
            }
        }
    }
}
