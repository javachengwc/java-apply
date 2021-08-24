package com.dubbo.pseudocode.common.serialize.java;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.serialize.ObjectInput;
import org.apache.dubbo.common.serialize.ObjectOutput;
import org.apache.dubbo.common.serialize.Serialization;
import org.apache.dubbo.common.serialize.java.JavaObjectInput;
import org.apache.dubbo.common.serialize.java.JavaObjectOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.dubbo.common.serialize.Constants.JAVA_SERIALIZATION_ID;

public class JavaSerialization implements Serialization {

    @Override
    public byte getContentTypeId() {
        return JAVA_SERIALIZATION_ID;
    }

    @Override
    public String getContentType() {
        return "x-application/java";
    }

    @Override
    public ObjectOutput serialize(URL url, OutputStream out) throws IOException {
        return new JavaObjectOutput(out);
    }

    @Override
    public ObjectInput deserialize(URL url, InputStream is) throws IOException {
        return new JavaObjectInput(is);
    }

}
