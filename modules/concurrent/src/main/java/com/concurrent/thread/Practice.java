package com.concurrent.thread;

/**
 * 多个客户群吃一个果盘水果
 * 果盘水果最多10个，吃完由服务员自动增加10个
 */
public class Practice {

	public static void main(String arg[]){

        //果盘
		Plate p1 = new Plate();

		Guest g1=new Guest("客群1",p1);
		
		Guest g2=new Guest("客群2",p1);
		
		Servant s1=new Servant("服务员",p1);

		s1.start();
		g1.start();
		g2.start();
	}
	
}
