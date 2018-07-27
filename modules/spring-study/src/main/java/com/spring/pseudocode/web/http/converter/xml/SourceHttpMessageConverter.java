package com.spring.pseudocode.web.http.converter.xml;

import com.spring.pseudocode.web.http.converter.AbstractHttpMessageConverter;
import com.spring.pseudocode.web.http.MediaType;

import javax.xml.transform.Source;

public class SourceHttpMessageConverter<T extends Source> extends AbstractHttpMessageConverter<T>
{
    public SourceHttpMessageConverter()
    {
        super(new MediaType[] { MediaType.APPLICATION_XML, MediaType.TEXT_XML, new MediaType("application", "*+xml") });
    }
}
