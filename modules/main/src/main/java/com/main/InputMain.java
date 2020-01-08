package com.main;

import java.util.Scanner;
import sun.misc.Signal;

public class InputMain {

  public static void main(String [] args ) {
    //ctrl+c
    Signal ctrlC = new Signal("INT");
    System.out.println(ctrlC.getName()+" "+ ctrlC.getNumber());
    System.out.println(ctrlC.toString());

    Scanner scan = new Scanner(System.in);
    // nextLine方式接收字符串
    System.out.println("nextLine方式接收：");
    // 判断是否还有输入
    if (scan.hasNextLine()) {
      String str2 = scan.nextLine();
      System.out.println(str2.length());
      char [] chars =str2.toCharArray();
      for(char c:chars) {
        System.out.println("char is "+ c);
      }
      System.out.println("输入的数据为：" + str2);
    }
    scan.close();
  }

}
