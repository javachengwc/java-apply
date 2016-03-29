package com.config;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.util.List;

/**
 * 读取配置文件相关
 * Commons-configuration 主要用来读取和设置配置文件.
 * 其中常用的包括*.properties和xml.查找相对路径的顺序为先查找project,再查找src
 */
public class Main {

    public static void main(String args []) throws Exception
    {

        // 操作 properties文件,直接读取src下的文件
        Configuration configuration = new PropertiesConfiguration("config.properties");
        System.out.println(configuration.getString("name"));
        // 逗号分割
        String[] arrays = configuration.getStringArray("member");
        System.out.println(ArrayUtils.toString(arrays));
        // 保存
        PropertiesConfiguration c2 = new PropertiesConfiguration("config2.properties");
        c2.setProperty("member", "phl,hxdg,bj,sanya");
        c2.save();
        // 保存到指定文件中--本例直接存在项目目录下
        c2.save(new File("configbak.properties"));
        // 当在工程目录下和src目录下，有同名配置文件时,读取工程下的;若工程下无文件，则再去查找src下是否有该文件
        Configuration cc = new PropertiesConfiguration("configbak.properties");
        System.out.println(cc.getString("member") + "名字");

        //--------------------------------------------------------

        // 操作 properties文件2
        Configuration config2 = new PropertiesConfiguration("config2.properties");
        String ip2 = config2.getString("ip");

        System.out.println("----print read array:");
        String[] colors = config2.getStringArray("colors.pie");
        for (int i = 0; i < colors.length; i++) {
            System.out.println(colors[i]);
        }

        System.out.println("----print read list:");
        List<?> colorList = config2.getList("colors.pie");
        for (int i = 0; i < colorList.size(); i++) {
            System.out.println(colorList.get(i));
        }

        //--------------------------------------------------------

        // 操作XML文件
        Configuration conf = new XMLConfiguration("test.xml");
        String ip = conf.getString("ip");
        String account = conf.getString("account");
        String password = conf.getString("password");
        List<?> roles = conf.getList("roles.role");
        System.out.println("IP: " + ip);
        System.out.println("Account: " + account);
        System.out.println("Password: " + password);
        for (int i = 0; i < roles.size(); i++) {
            System.out.println("Role: " + roles.get(i));
        }

        //---------------------------------------------------------

        // 操作XML文件2
        XMLConfiguration config = new XMLConfiguration("my.xml");
        // 获取节点值 路径中不包括根节点名
        String s1 = config.getString("disks.u-disk");
        System.out.println(s1);
        // 获取节点属性值
        String s2 = config.getString("raid[@name]");
        System.out.println(s2);
        // 获得动态属性
        String s3 = config.getString("disks.soft-disk");
        System.out.println(s3);
        // 获取列表
        List rs = config.getList("raid.r");
        System.out.println(rs);
        config.save(new File("e://my.xml"));

        //------------------------------------------------------

        System.out.println("----------------"+SysConfig.getValue("gain_time_start"));
        System.out.println("----------------"+SysConfig.getValue("gain_time_end"));
    }
}
