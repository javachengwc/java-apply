package com.manage.rbac;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.springdubbo.ApplicationStarter;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "120.79.197.191:8858",namespace = "dev"))
@NacosPropertySource(dataId = "applocation.properties", groupId = "manage-rbac-rest",autoRefreshed = true,type = ConfigType.YAML,first = true)
public class ManageRbacApplication {

    public static void main(String[] args) {
        ApplicationStarter.run(ManageRbacApplication.class, args);
    }

}
