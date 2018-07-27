package com.spring.pseudocode.web.http;

import com.spring.pseudocode.core.util.MimeType;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;

public class MediaType extends MimeType implements Serializable {

    public static final MediaType ALL = valueOf("*/*");

    public static final MediaType APPLICATION_XML = valueOf("application/xml");

    public static final MediaType TEXT_XML = valueOf("text/xml");

    public static final MediaType APPLICATION_JSON_UTF8 = valueOf("application/json;charset=UTF-8");

    public static final MediaType APPLICATION_FORM_URLENCODED = valueOf("application/x-www-form-urlencoded");

    public static final MediaType MULTIPART_FORM_DATA = valueOf("multipart/form-data");

    public MediaType(String type)
    {
        super(type);
    }

    public MediaType(String type, String subtype)
    {
        super(type, subtype, Collections.EMPTY_MAP);
    }

    public MediaType(String type, String subtype, Charset charset)
    {
        super(type, subtype, charset);
    }

    public static MediaType valueOf(String value)
    {
        return parseMediaType(value);
    }

    public static MediaType parseMediaType(String mediaType)
    {
        //...........
        return null;
    }

}
