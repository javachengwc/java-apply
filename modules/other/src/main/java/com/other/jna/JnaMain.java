package com.other.jna;

//如果执行报错java.lang.UnsatisfiedLinkError: Unable to load library 'xxx': libxxx.so: cannot open shared object file:No such file or directory
//需要把so库所在路径(比如:/usr/local/lib)加到系统寻找so的路径中，方法为:
//vim /etc/ld.so.conf
//    /usr/local/lib
//ldconfig
public class JnaMain {

    public static void main(String [] args ) {
        String param ="str";
        byte [] rtData  = new byte[100];
        int result = CLibrary.INSTANTCE.xxx(param, rtData);
        String rt = new String(rtData);
        System.out.println("JnaMain invoke 本地c语言so库方法xxx的结果为:"+rt);

    }
}
