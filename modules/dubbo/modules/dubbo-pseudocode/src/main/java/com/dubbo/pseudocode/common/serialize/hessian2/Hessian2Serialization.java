package com.dubbo.pseudocode.common.serialize.hessian2;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.serialize.ObjectInput;
import org.apache.dubbo.common.serialize.ObjectOutput;
import org.apache.dubbo.common.serialize.Serialization;
import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectInput;
import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.dubbo.common.serialize.Constants.HESSIAN2_SERIALIZATION_ID;

public class Hessian2Serialization implements Serialization {

    @Override
    public byte getContentTypeId() {
        return HESSIAN2_SERIALIZATION_ID;
    }

    @Override
    public String getContentType() {
        return "x-application/hessian2";
    }

    @Override
    public ObjectOutput serialize(URL url, OutputStream out) throws IOException {
        return new Hessian2ObjectOutput(out);
    }

    @Override
    public ObjectInput deserialize(URL url, InputStream is) throws IOException {
        return new Hessian2ObjectInput(is);
    }

}
