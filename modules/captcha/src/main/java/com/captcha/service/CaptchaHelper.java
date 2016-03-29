package com.captcha.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.captcha.dto.Captcha;

public class CaptchaHelper {
	
	private static Logger logger = Logger.getLogger(CaptchaHelper.class);
	
	private static Random random = new Random();
    private static String randString = "0123456789abc";//随机产生的字符串
    
    private static int width = 80;//图片宽
    private static int height = 36;//图片高
    private static int stringNum = 4;//随机产生字符数量
    
    /**
     * 获得字体
     */
    private static Font getFont(){
        return new Font("Times New Roman",Font.ROMAN_BASELINE,32);
    }
    
    /**
     * 获得颜色
     */
    private static Color getRandColor(int fc,int bc){
        if(fc > 255)
            fc = 255;
        if(bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc-fc-16);
        int g = fc + random.nextInt(bc-fc-14);
        int b = fc + random.nextInt(bc-fc-18);
        return new Color(r,g,b);
    }
    
    /**
     * 生成随机图片
     */
    public static Captcha render(String randStr) {
    	
    	if(StringUtils.isBlank(randStr))
    	{
    		randStr=randString;
    	}
        //BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width,height,BufferedImage.SCALE_SMOOTH);
        Graphics gg = image.getGraphics();//产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        Graphics2D g = (Graphics2D)gg;
        g.setColor(new Color(180, 221, 247));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman",Font.ROMAN_BASELINE,10));
        //绘制干扰线
        int lineSize =random.nextInt(3)+2;
        for(int i=0;i<=lineSize;i++){
            drowLine(g);
        }
        //绘制燥点
        drawRect(g);
        //绘制随机字符
        String randomString = "";
        g.setFont(getFont());
        
        for(int i=1;i<=stringNum;i++){
            randomString=drowString(g,randStr,randomString,i);
        }
        g.dispose();
        return new Captcha(randomString,image);
    }
    
    
//    private static String drowString(Graphics g, String randStr, String randomString, int i)
//    {
//		  g.setColor(new Color(random.nextInt(255), random.nextInt(100), random.nextInt(200)));
//		  String rand = String.valueOf(getRandomString(randStr, random.nextInt(randStr.length())));
//		  randomString = randomString + rand;
//		  g.translate(random.nextInt(3), random.nextInt(3));
//		  FontMetrics fm = g.getFontMetrics();
//		  int stringAscent = fm.getAscent();
//		  int posX = 13 * i + random.nextInt(10) - 5;
//		  if (i == 1)
//		  {
//		    posX = 6;
//		  }
//		  logger.error("rand:"+rand);
//		  g.drawString(rand, posX, 10 + stringAscent / 2);
//		  return randomString;
//    }
    
    /**
     * 绘制字符串
     */
    private static String drowString(Graphics2D g,String randStr,String randomString,int i){
        g.setColor(new Color(random.nextInt(255),random.nextInt(100),random.nextInt(200)));
        String rand = String.valueOf(getRandomString(randStr,random.nextInt(randStr.length())));
        randomString +=rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        FontMetrics fm = g.getFontMetrics();
        int stringAscent = fm.getAscent();//26
        int posX=13*i+random.nextInt(6)-3;
        if(i==1)
        {
        	posX=6;
        }
        int posY=10+stringAscent/2;
        if(random.nextInt(3)==0)   //1/3的几率
        {
	        int theta = new Random().nextInt()%30;//获取-30到30的数
	        g.rotate(theta*Math.PI/180, posX, posY);//设置旋转角度（弧度）
	        g.drawString(rand,posX,posY);
	        g.rotate(-theta*Math.PI/180, posX, posY);
        }else
        {
        	g.drawString(rand,posX,posY);
        }
        return randomString;
    }
    
    /**
     * 绘制干扰线
     */
    private static void drowLine(Graphics2D g){
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(60);
        int yl = random.nextInt(20);
        g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        g.drawLine(x, y, xl, yl);
    }
    
    /**
     * 绘制燥点
     */
    private static void drawRect(Graphics2D g)
    {
        int sum = random.nextInt(50);
        if(sum<20)
        {
        	sum=20;
        }
        for (int i = 0;i<sum;i++)
        {
            g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		    //g.drawRoundRect(random.nextInt(width),random.nextInt(height), 1,0,1,0);
		    //g.drawRect(random.nextInt(width),random.nextInt(height), 1,0);
            int x=random.nextInt(width);
            int y=random.nextInt(height);
            g.drawLine(x,y, x,y);
        }
    }
    
    /**
     * 获取随机的字符
     */
    public static String getRandomString(String randStr,int num){
        return String.valueOf(randStr.charAt(num));
    }

}
