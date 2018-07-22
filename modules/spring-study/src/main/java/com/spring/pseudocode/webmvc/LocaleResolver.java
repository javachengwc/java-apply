package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public abstract interface LocaleResolver
{
    public abstract Locale resolveLocale(HttpServletRequest request);

    public abstract void setLocale(HttpServletRequest request, HttpServletResponse response, Locale paramLocale);
}
