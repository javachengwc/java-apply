package com.pseudocode.cloud.openfeign.core;

import com.pseudocode.cloud.context.named.NamedContextFactory;

//FeignContext使用FeignClientsConfiguration进行配置
//在FeignClientsConfiguration中会把feignClient相关的配置绑定到FeignContext中
public class FeignContext extends NamedContextFactory<FeignClientSpecification> {

    public FeignContext() {
        super(FeignClientsConfiguration.class, "feign", "feign.client.name");
    }

}
