package com.spring.pseudocode.web.http;

import java.io.IOException;
import java.io.InputStream;

public abstract interface HttpInputMessage extends HttpMessage
{
    public abstract InputStream getBody() throws IOException;
}
