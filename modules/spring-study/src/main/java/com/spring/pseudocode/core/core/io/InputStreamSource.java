package com.spring.pseudocode.core.core.io;

import java.io.IOException;
import java.io.InputStream;

public abstract interface InputStreamSource
{
    public abstract InputStream getInputStream() throws IOException;
}
