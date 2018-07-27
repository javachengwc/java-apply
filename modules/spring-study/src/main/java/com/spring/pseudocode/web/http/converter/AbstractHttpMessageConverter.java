package com.spring.pseudocode.web.http.converter;

import com.spring.pseudocode.web.http.HttpOutputMessage;
import com.spring.pseudocode.web.http.MediaType;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractHttpMessageConverter<T> implements HttpMessageConverter<T> {

    private List<MediaType> supportedMediaTypes = Collections.emptyList();

    protected AbstractHttpMessageConverter()
    {
    }

    protected AbstractHttpMessageConverter(MediaType supportedMediaType)
    {
        setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
    }

    protected AbstractHttpMessageConverter(MediaType[] supportedMediaTypes)
    {
        setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
    }

    public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes)
    {
        this.supportedMediaTypes = new ArrayList(supportedMediaTypes);
    }

    public boolean canRead(Class<?> paramClass, MediaType mediaType) {
        return false;
    }

    public boolean canWrite(Class<?> paramClass, MediaType mediaType) {
        return false;
    }

    public List<MediaType> getSupportedMediaTypes() {
        return null;
    }

    public T read(Class<? extends T> paramClass, HttpInputMessage httpInputMessage) throws IOException {
        return null;
    }

    public void write(T paramT, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException {

    }
}
