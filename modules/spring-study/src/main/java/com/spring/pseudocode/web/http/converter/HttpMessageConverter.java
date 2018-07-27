package com.spring.pseudocode.web.http.converter;


import com.spring.pseudocode.web.http.HttpOutputMessage;
import com.spring.pseudocode.web.http.MediaType;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.util.List;

public abstract interface HttpMessageConverter<T>
{
    public abstract boolean canRead(Class<?> paramClass, MediaType mediaType);

    public abstract boolean canWrite(Class<?> paramClass, MediaType mediaType);

    public abstract List<MediaType> getSupportedMediaTypes();

    public abstract T read(Class<? extends T> paramClass, HttpInputMessage httpInputMessage)  throws IOException;

    public abstract void write(T paramT, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException;
}
