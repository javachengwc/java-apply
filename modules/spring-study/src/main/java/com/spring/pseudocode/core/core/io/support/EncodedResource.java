package com.spring.pseudocode.core.core.io.support;

import com.spring.pseudocode.core.core.io.InputStreamSource;
import com.spring.pseudocode.core.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class EncodedResource implements InputStreamSource
{
    private final Resource resource;

    private final String encoding;

    private final Charset charset;

    public EncodedResource(Resource resource)
    {
        this(resource, null, null);
    }

    public EncodedResource(Resource resource, String encoding)
    {
        this(resource, encoding, null);
    }

    public EncodedResource(Resource resource, Charset charset)
    {
        this(resource, null, charset);
    }

    private EncodedResource(Resource resource, String encoding, Charset charset)
    {
        this.resource = resource;
        this.encoding = encoding;
        this.charset = charset;
    }

    public final Resource getResource()
    {
        return this.resource;
    }

    public final String getEncoding()
    {
        return this.encoding;
    }

    public final Charset getCharset()
    {
        return this.charset;
    }

    public boolean requiresReader()
    {
        return (this.encoding != null) || (this.charset != null);
    }

    public Reader getReader() throws IOException
    {
        if (this.charset != null) {
            return new InputStreamReader(this.resource.getInputStream(), this.charset);
        }
        if (this.encoding != null) {
            return new InputStreamReader(this.resource.getInputStream(), this.encoding);
        }

        return new InputStreamReader(this.resource.getInputStream());
    }

    public InputStream getInputStream() throws IOException
    {
        return this.resource.getInputStream();
    }

    public boolean equals(Object other)
    {
        //...
        return false;
    }

    public int hashCode()
    {
        return this.resource.hashCode();
    }

    public String toString()
    {
        return this.resource.toString();
    }
}
