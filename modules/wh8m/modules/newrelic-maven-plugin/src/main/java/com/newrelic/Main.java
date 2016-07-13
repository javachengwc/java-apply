package com.newrelic;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;

/**
 * 运行入口
 * mvn clean  compile dependency:copy-dependencies install  -Dmaven.test.skip=true.
 */
public class Main {


    public static void init()
    {
        //日志配置初始化
        String configUrl =  Thread.currentThread().getContextClassLoader().getResource("log4j.properties").getPath();
        PropertyConfigurator.configure(configUrl);
    }

    //xml,jar,path
    //xml,path->service,serviceimpl
    //jar ->service,reflement method types ,serviceimpl
    //write xml template,template下面
    public static void main(String args [])
    {
        // init();
//        args = new String [4];
//        args[0]="F:\\project\\modules\\data-service\\src\\main\\resources\\spring\\*";
//        args[1]="all";
//        args[2]="F:\\project\\modules\\data-service\\target\\dependency\\,F:\\project\\modules\\data-service\\target\\classes";
//        args[3]="F:\\data";

        if(args==null || args.length<2)
        {
            throw new RuntimeException("args is lacked ,at list 2 args");
        }
        //xml路径
        String xmlPath =args[0];

        //构建的结果集
        String resultFlag="all";
        if(args.length>=2)
        {
            resultFlag=args[1];
        }

        //jar包路径
        String jarPath =null;
        if(args.length>=3)
        {
            jarPath=args[2];
            if(!StringUtils.isBlank(jarPath) && "null".equalsIgnoreCase(jarPath))
            {
                jarPath=null;
            }
        }
        //输出目录
        String outPath="";
        if(args.length>=4)
        {
            outPath=args[3];
            if(!StringUtils.isBlank(outPath) && "null".equalsIgnoreCase(outPath))
            {
                outPath=null;
            }
        }

        System.out.println("----------Main start----------");

        System.out.println("----------Main args xmpPath="+xmlPath+" \r\n"+
                           "------------------- resultFlag="+resultFlag+" \r\n"+
                           "------------------- jarPath="+jarPath+" \r\n"+
                           "------------------- outPath="+outPath);
        Executor executor = new Executor(xmlPath,resultFlag,jarPath,outPath);
        executor.exe();
        System.out.println("----------Main end----------");

    }

}
