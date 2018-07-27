package com.spring.pseudocode.web.http.converter.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.pseudocode.web.http.MediaType;
import com.spring.pseudocode.web.http.converter.AbstractHttpMessageConverter;

import java.nio.charset.Charset;

//public class MappingJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter
public class MappingJackson2HttpMessageConverter extends AbstractHttpMessageConverter<Object>
{
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    protected ObjectMapper objectMapper;

    public MappingJackson2HttpMessageConverter()
    {
//        this(Jackson2ObjectMapperBuilder.json().build());
    }

    public MappingJackson2HttpMessageConverter(ObjectMapper objectMapper)
    {
        super( new MediaType[] { MediaType.APPLICATION_JSON_UTF8, new MediaType("application", "*+json", DEFAULT_CHARSET) });
        this.objectMapper=objectMapper;
    }

}