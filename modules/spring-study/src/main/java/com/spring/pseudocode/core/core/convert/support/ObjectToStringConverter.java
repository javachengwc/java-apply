package com.spring.pseudocode.core.core.convert.support;

import com.spring.pseudocode.core.core.convert.converter.Converter;

final class ObjectToStringConverter implements Converter<Object, String>
{
    public String convert(Object source)
    {
        return source.toString();
    }
}
