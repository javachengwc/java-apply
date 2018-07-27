package com.spring.pseudocode.core.util;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

public class MimeType {

    public MimeType(String type)
    {
        this(type, "*");
    }

    public MimeType(String type, String subtype)
    {
        this(type, subtype, Collections.EMPTY_MAP);
    }

    public MimeType(String type, String subtype, Charset charset)
    {
        this(type, subtype, Collections.singletonMap("charset", charset.name()));
    }


    public MimeType(String type, String subtype, Map<String, String> parameters)
    {
       //..............................
    }

    public static MimeType valueOf(String value)
    {
        //return MimeTypeUtils.parseMimeType(value);
        return null;
    }
}
