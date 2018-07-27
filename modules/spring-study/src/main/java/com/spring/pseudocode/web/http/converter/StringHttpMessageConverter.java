package com.spring.pseudocode.web.http.converter;

import com.spring.pseudocode.web.http.MediaType;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class StringHttpMessageConverter extends AbstractHttpMessageConverter<String> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

    private final Charset defaultCharset;
    private final List<Charset> availableCharsets;
    private boolean writeAcceptCharset = true;

    public StringHttpMessageConverter() {
        this(DEFAULT_CHARSET);
    }

    public StringHttpMessageConverter(Charset defaultCharset) {
        super(new MediaType[]{new MediaType("text", "plain", defaultCharset), MediaType.ALL});
        this.defaultCharset = defaultCharset;
        this.availableCharsets = new ArrayList(Charset.availableCharsets().values());
    }

    public void setWriteAcceptCharset(boolean writeAcceptCharset) {
        this.writeAcceptCharset = writeAcceptCharset;
    }

    public boolean supports(Class<?> clazz) {
        return String.class == clazz;
    }
}