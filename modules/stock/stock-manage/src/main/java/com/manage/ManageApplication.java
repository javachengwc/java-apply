package com.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ManageApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
        System.out.println("-----------股票管理平台启动成功------------------\n");
    }
}
