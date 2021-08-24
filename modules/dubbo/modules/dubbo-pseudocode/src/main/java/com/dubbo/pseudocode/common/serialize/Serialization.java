package com.dubbo.pseudocode.common.serialize;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.common.serialize.ObjectInput;
import org.apache.dubbo.common.serialize.ObjectOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SPI("hessian2")
public interface Serialization {

    byte getContentTypeId();

    String getContentType();

    @Adaptive
    ObjectOutput serialize(URL url, OutputStream output) throws IOException;

    @Adaptive
    ObjectInput deserialize(URL url, InputStream input) throws IOException;

}
