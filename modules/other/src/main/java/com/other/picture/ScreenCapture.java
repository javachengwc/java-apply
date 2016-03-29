package com.other.picture;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * 屏幕截图转存png图片
 */
public class ScreenCapture {
    public static void main(String args[]) throws AWTException, IOException {

        BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        File file = new File("e:/tmp/screencapture.png");
        ImageIO.write(screencapture, "png", file);
    }
}
