package com.pseudocode.netflix.feign.core.codec;


import java.io.IOException;
import java.lang.reflect.Type;

import com.pseudocode.netflix.feign.core.FeignException;
import com.pseudocode.netflix.feign.core.Response;
import com.pseudocode.netflix.feign.core.Util;

import javax.websocket.DecodeException;

public interface Decoder {

    Object decode(Response response, Type type) throws IOException, DecodeException, FeignException;

    public class Default extends StringDecoder {

        @Override
        public Object decode(Response response, Type type) throws IOException {
            if (response.status() == 404) return Util.emptyValueOf(type);
            if (response.body() == null) return null;
            if (byte[].class.equals(type)) {
                return Util.toByteArray(response.body().asInputStream());
            }
            return super.decode(response, type);
        }
    }
}
