package com.spring.pseudocode.webmvc;

import java.util.Locale;

public abstract interface ViewResolver
{
    public abstract View resolveViewName(String paramString, Locale paramLocale) throws Exception;
}
