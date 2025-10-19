package com.util.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * maven下编译报错程序包com.sun.image.codec.jpeg不存在,
 * 是因为java_home下面的lib/dt.jar中没有这个jar文件，导致编译失败
 * 通过配置maven-compiler-plugin插件可以解决此问题
 */
public class ImageCompressUtil
{
	/**
	 * 压缩质量
	 */
	public static final float QUALITY_DEFAULT =0.75f;
	
	/**
	 * 生成缩略图
	 * 
	 * @param source 原图的输入流
	 * @param fileExtName 原图的后缀名
	 * @param size 新图的像素大小
	 * @param flag 不进行大小判断，一定执行图片压缩或放大
	 * @return
	 */
	public static byte[] compress(InputStream source, String fileExtName,
		int size, boolean flag)
		throws Exception
	{
		
		return compress(source, fileExtName, size, QUALITY_DEFAULT, flag);
	}
	
	/**
	 * @param source 原图的输入流
	 * @param fileExtName 原图的后缀名
	 * @param size 新图的像素大小
	 * @param qt 图片压缩质量，最大值为1，当数值越大时，质量越好，同时文件也越大
	 * @param flag 是否进行大小判断，一定执行图片压缩或放大
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(InputStream source, String fileExtName,
		int size, float qt, boolean flag)
		throws Exception
	{
		BufferedImage bis = ImageIO.read(source);
		
		int w = bis.getWidth();
		int h = bis.getHeight();
		// 高宽都小于size就不压缩
		if(w < size && h < size && flag == false)
		{
			return null;
		}
		
		int nw = size;
		int nh = (nw * h) / w;
		if(nh > size)
		{
			nh = size;
			nw = (nh * w) / h;
		}
		if(w <= size && h <= size)
		{
			nh = h;
			nw = w;
		}
		
		BufferedImage bid = new BufferedImage(nw, nh,
			BufferedImage.TYPE_INT_RGB);
		Graphics2D draw = bid.createGraphics();
		draw.drawImage(bis.getScaledInstance(nw, nh, Image.SCALE_SMOOTH), 0, 0,
			nw, nh, null);
		draw.dispose();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//		JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(bid);
//		/* 压缩质量 */
//		jep.setQuality(qt, true);
//		encoder.encode(bid, jep);

		// ImageIO.write(bid, fileExtName, out);//这个默认质量就是0.75
		return out.toByteArray();
	}
	
	public static void main(String[] args)
		throws Exception
	{
		
		for(int j=1;j<4;j++){
			FileInputStream in = new FileInputStream(
			"F:/首页图片/ym/"+j+".png");
			 byte[] file_buff;
			 ByteArrayOutputStream out1 = new ByteArrayOutputStream(4096);
			 byte[] b1 = new byte[ 4096 ];
			 int n;
			 while((n = in.read(b1)) != -1)
			 {
			 out1.write(b1, 0, n);
			 }
			 out1.close();
			 file_buff = out1.toByteArray();
			 
			
			byte[] b = null;
			
			for(int i = 2; i < 10; i++)
			{
				InputStream suitIn = new ByteArrayInputStream(file_buff);
				// BufferedImage bis = ImageIO.read(in);
				// File file = new File("E:/img/newimage.png");
				// ImageIO.write(bis, "png", file);
				long start = System.currentTimeMillis();
				b = ImageCompressUtil.compress(suitIn, "jpg", 240, i / 10f, true);
				
				long end = System.currentTimeMillis();
				System.out.println("total: " + (end - start));
				
				FileOutputStream out = new FileOutputStream(
				"F:/首页图片/ym/res/test"+j+"_"+i*10+".jpg");
				out.write(b);
				out.close();
				
			}
		}
	}
}
