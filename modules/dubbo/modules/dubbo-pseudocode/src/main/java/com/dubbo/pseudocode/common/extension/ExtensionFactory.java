package com.dubbo.pseudocode.common.extension;

import org.apache.dubbo.common.extension.SPI;

@SPI
public interface ExtensionFactory {

    <T> T getExtension(Class<T> type, String name);

}
