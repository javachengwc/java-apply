package com.other.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Hack extends JPanel {

    public final static int WIDTH = 1440;// 改窗口大小,最好和桌面分辨率一样,装B才能到极致
    public final static int HEIGHT = 900;
    public final static int LENGTH = 40;
    private List< Colorful> c = new ArrayList< Colorful>();
    private Random r = new Random();
    public Hack() {
        for (int i = 0; i < LENGTH; i++) {
            int x = i * LENGTH + 12;
            int speed = r.nextInt(4) + 5;// 改数字下落的速度
            Colorful o = new Colorful(x, speed);
            c.add(o);
        }
        new Thread(new PaintThread()).start();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("黑客帝国");
        Hack hack = new Hack();
        frame.add(hack);
        frame.setSize(WIDTH, HEIGHT);
        frame.setUndecorated(true);// 这句注释掉就没有窗口了,也是为了装X
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private class PaintThread implements Runnable {// 主要用来画图的线程
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(1000 / 24);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Hack.WIDTH, Hack.WIDTH);
        Iterator< Colorful> it = c.iterator();
        while (it.hasNext()) {
            Colorful o = it.next();
            o.draw(g);
        }
    }
}
