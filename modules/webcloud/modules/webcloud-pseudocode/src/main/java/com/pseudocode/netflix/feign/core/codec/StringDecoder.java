package com.pseudocode.netflix.feign.core.codec;


import java.io.IOException;
import java.lang.reflect.Type;

import com.pseudocode.netflix.feign.core.Response;
import com.pseudocode.netflix.feign.core.Util;

import static java.lang.String.format;

public class StringDecoder implements Decoder {

    @Override
    public Object decode(Response response, Type type) throws IOException {
        Response.Body body = response.body();
        if (body == null) {
            return null;
        }
        if (String.class.equals(type)) {
            return Util.toString(body.asReader());
        }
        throw new DecodeException(format("%s is not a type supported by this decoder.", type));
    }
}

