package com.pseudocode.netflix.ribbon.core.client;

import java.io.Closeable;
import java.net.URI;
import java.util.Map;

public interface IResponse extends Closeable {

    Object getPayload() throws ClientException;

    boolean hasPayload();

    boolean isSuccess();

    URI getRequestedURI();

    Map<String, ?> getHeaders();
}
