package com.pseudocode.cloud.zuul;

import java.io.File;

public interface DynamicCodeCompiler {

    Class compile(String var1, String var2) throws Exception;

    Class compile(File var1) throws Exception;
}
