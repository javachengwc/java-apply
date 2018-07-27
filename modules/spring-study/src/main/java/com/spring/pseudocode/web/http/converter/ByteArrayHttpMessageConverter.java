package com.spring.pseudocode.web.http.converter;

import com.spring.pseudocode.web.http.HttpInputMessage;
import com.spring.pseudocode.web.http.HttpOutputMessage;
import com.spring.pseudocode.web.http.MediaType;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteArrayHttpMessageConverter extends AbstractHttpMessageConverter<byte[]>
{
    public ByteArrayHttpMessageConverter()
    {
        super(new MediaType[] { new MediaType("application", "octet-stream"), MediaType.ALL });
    }

    public boolean supports(Class<?> clazz)
    {
        return Byte.class == clazz;
    }

    public byte[] readInternal(Class<? extends byte[]> clazz, HttpInputMessage inputMessage) throws IOException
    {
        long contentLength = inputMessage.getHeaders().getContentLength();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(contentLength >= 0L ? (int)contentLength : 4096);

        StreamUtils.copy(inputMessage.getBody(), bos);
        return bos.toByteArray();
    }

    protected Long getContentLength(byte[] bytes, MediaType contentType)
    {
        return Long.valueOf(bytes.length);
    }

    protected void writeInternal(byte[] bytes, HttpOutputMessage outputMessage) throws IOException
    {
        StreamUtils.copy(bytes, outputMessage.getBody());
    }
}