package com.captcha.service;

import com.util.enh.RandomUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 生成随机字符小程序
 */
public class CaptchaImage {

	public static BufferedImage createBufferedImage(String text) throws IOException {
		if (text.getBytes().length > 4)
			throw new IllegalArgumentException("The length of param text cannot exceed 4.");
		int width = 50;
		int height = 23;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(getRandColor(200, 240));
		Random random = new Random();
		for (int i = 0; i < 65; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(20);
			int yl = random.nextInt(40);
			g.drawLine(x, y, x + xl, y + yl);
		}
		Font mFont = new Font(null, Font.BOLD, 17);
		g.setFont(mFont);
		g.setColor(getRandColor(88, 244));
		g.drawString(text, 4, 19);
		return bi;
	}

	/**
	 * 根据要求的数字生成图片,背景为白色,字体大小16,字体颜色黑色粗体
	 * 
	 * @param text
	 *            要生成的文字
	 * @param out
	 *            输出流
	 * @throws java.io.IOException
	 */
	public static void render(String text, OutputStream out) throws IOException {
		if (text.getBytes().length > 4)
			throw new IllegalArgumentException("The length of param text cannot exceed 4.");
		int width = 50;
		int height = 23;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(getRandColor(200, 240));
		Random random = new Random();
		for (int i = 0; i < 65; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(20);
			int yl = random.nextInt(40);
			g.drawLine(x, y, x + xl, y + yl);
		}
		Font mFont = new Font(null, Font.BOLD, 17);
		g.setFont(mFont);
		g.setColor(getRandColor(88, 244));
		g.drawString(text, 4, 19);
		ImageIO.write(bi, "jpg", out);
	}

	/**
	 * 给定范围获得随机颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	public static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * @param args
	 * @throws java.io.IOException
	 * @throws java.io.FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String num = RandomUtil.getRandomString(4);
        System.out.println(num);
		render(num, new FileOutputStream("D:\\test.jpg"));
	}

}
