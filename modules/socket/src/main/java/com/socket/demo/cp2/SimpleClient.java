package com.socket.demo.cp2;
import java.io.*;
import java.net.*;
public class SimpleClient {
  public static void main(String args[])throws Exception {
    Socket s = new Socket("localhost",8000);
    //s.setSoLinger(true,0);  //Socket关闭后，底层Socket立即关闭
    //s.setSoLinger(true,3600);  //Socket关闭后，底层Socket延迟3600秒再关闭
    OutputStream out=s.getOutputStream();
    StringBuffer sb=new StringBuffer();
    for(int i=0;i<10000;i++)sb.append(i);
    out.write(sb.toString().getBytes());  //发送一万个字符
    System.out.println("开始关闭Socket");
    long begin=System.currentTimeMillis();
    s.close();
    long end=System.currentTimeMillis();
    System.out.println("关闭Socket所用的时间为:"+(end-begin)+"ms");    
  }
}
