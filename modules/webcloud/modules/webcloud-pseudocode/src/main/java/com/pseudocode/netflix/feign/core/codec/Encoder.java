package com.pseudocode.netflix.feign.core.codec;

import java.lang.reflect.Type;

import com.pseudocode.netflix.feign.core.RequestTemplate;
import com.pseudocode.netflix.feign.core.Util;

import static java.lang.String.format;

public interface Encoder {

    Type MAP_STRING_WILDCARD = Util.MAP_STRING_WILDCARD;

    void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException;

    class Default implements Encoder {

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) {
            if (bodyType == String.class) {
                template.body(object.toString());
            } else if (bodyType == byte[].class) {
                template.body((byte[]) object, null);
            } else if (object != null) {
                throw new EncodeException(format("%s is not a type supported by this encoder.", object.getClass()));
            }
        }
    }
}