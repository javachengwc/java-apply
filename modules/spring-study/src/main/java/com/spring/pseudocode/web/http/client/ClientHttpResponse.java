package com.spring.pseudocode.web.http.client;


import java.io.Closeable;
import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;

public interface ClientHttpResponse extends HttpInputMessage, Closeable {

    HttpStatus getStatusCode() throws IOException;

    String getStatusText() throws IOException;

    @Override
    void close();

}
