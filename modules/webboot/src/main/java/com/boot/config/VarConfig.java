package com.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 读取var.properties配置映射到此类中。
 */
@Configuration
@ConfigurationProperties(prefix = "var")
@PropertySource(value ="classpath:var.properties",ignoreResourceNotFound=true)
public class VarConfig {

    public static Map<String, String> a = new HashMap<String,String>();

    public static Map<String, String> b = new HashMap<String,String>();

    public static String aa;

    public  Map<String, String> getA() {
        return a;
    }

    public  void setA(Map<String, String> a) {
        VarConfig.a = a;
    }

    public  Map<String, String> getB() {
        return b;
    }

    public  void setB(Map<String, String> b) {
        VarConfig.b = b;
    }

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        VarConfig.aa = aa;
    }
}
