package com.manage.rbac;

import com.springdubbo.dubbo.DubboStarter;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@DubboComponentScan(basePackages = "com.manage.rbac.provider.impl")
@MapperScan("com.manage.rbac.dao")
public class ManageRbacDubboApplication {

    public static void main(String[] args) {
        DubboStarter.run(ManageRbacDubboApplication.class, args);
    }

}
