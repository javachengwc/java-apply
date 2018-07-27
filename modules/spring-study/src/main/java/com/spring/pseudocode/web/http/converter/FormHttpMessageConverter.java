package com.spring.pseudocode.web.http.converter;

import com.spring.pseudocode.core.util.MultiValueMap;
import com.spring.pseudocode.web.http.HttpOutputMessage;
import com.spring.pseudocode.web.http.MediaType;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FormHttpMessageConverter implements HttpMessageConverter<MultiValueMap<String, ?>> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Charset charset = DEFAULT_CHARSET;

    private Charset multipartCharset;

    private List<MediaType> supportedMediaTypes = new ArrayList();

    private List<HttpMessageConverter<?>> partConverters = new ArrayList();

    public FormHttpMessageConverter() {

        this.supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        this.supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);

        this.partConverters.add(new ByteArrayHttpMessageConverter());
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        this.partConverters.add(stringHttpMessageConverter);
        this.partConverters.add(new ResourceHttpMessageConverter());
    }

    public List<MediaType> getSupportedMediaTypes()
    {
        return Collections.unmodifiableList(this.supportedMediaTypes);
    }

    public void addPartConverter(HttpMessageConverter<?> partConverter)
    {
        this.partConverters.add(partConverter);
    }

    public boolean canRead(Class<?> paramClass, MediaType mediaType) {
        return false;
    }

    public boolean canWrite(Class<?> paramClass, MediaType mediaType) {
        return false;
    }

    public MultiValueMap<String, ?> read(Class<? extends MultiValueMap<String, ?>> paramClass, HttpInputMessage httpInputMessage) throws IOException {
        return null;
    }

    public void write(MultiValueMap<String, ?> paramT, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException {

    }

}
