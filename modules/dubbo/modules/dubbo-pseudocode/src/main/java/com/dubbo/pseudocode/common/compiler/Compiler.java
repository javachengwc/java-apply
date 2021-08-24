package com.dubbo.pseudocode.common.compiler;

import org.apache.dubbo.common.extension.SPI;

@SPI("javassist")
public interface Compiler {

    Class<?> compile(String code, ClassLoader classLoader);
}
