package com.dubbo.pseudocode.registry;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.registry.Registry;

@SPI("dubbo")
public interface RegistryFactory {

    @Adaptive({"protocol"})
    Registry getRegistry(URL url);

}
