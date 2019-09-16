package com.pseudocode.netflix.ribbon.httpclient.http;

import com.google.common.reflect.TypeToken;
import com.pseudocode.netflix.ribbon.core.client.IResponse;

import java.io.Closeable;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public interface HttpResponse extends IResponse, Closeable {

    public int getStatus();

    public String getStatusLine();

    /**
     * @see #getHttpHeaders()
     */
    @Override
    @Deprecated
    public Map<String, Collection<String>> getHeaders();

    public HttpHeaders getHttpHeaders();

    public void close();

    public InputStream getInputStream();

    public boolean hasEntity();

    public <T> T getEntity(Class<T> type) throws Exception;

    public <T> T getEntity(Type type) throws Exception;

    /**
     * @deprecated use {@link #getEntity(Type)}
     */
    @Deprecated
    public <T> T getEntity(TypeToken<T> type) throws Exception;
}

