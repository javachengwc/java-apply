package com.util.base;

public class InputUtil {

  //DEL
  public static byte Del=0x7f;

  //Control+\
  public static byte Ctrl_Backslash = 0x1c;

  //获得Ctrl+A-Z的byte
  public static byte ctrlKey(char c) {
    return (byte)((byte)c - (byte)'A' + 1);
  }


}
