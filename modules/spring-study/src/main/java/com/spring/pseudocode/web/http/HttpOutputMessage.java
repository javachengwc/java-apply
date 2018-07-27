package com.spring.pseudocode.web.http;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface HttpOutputMessage extends HttpMessage
{
    public abstract OutputStream getBody() throws IOException;
}
