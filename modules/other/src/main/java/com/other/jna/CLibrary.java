package com.other.jna;
//
//import com.sun.jna.Library;
//import com.sun.jna.Native;
//
//public interface CLibrary extends Library {
//
//    //库文件都是lib**.so，但是在加载.so库文件的时候是不需要lib前缀以及.so后缀的。
//    //jna包在3.0.9版本及之前版本中加载so库的方法是Native.loadLibrary，最新版本此方法已过期
//    CLibrary INSTANTCE = (CLibrary) Native.load("xxx", CLibrary.class);
//
//    public int xxx(String str,byte[] data);
//}
