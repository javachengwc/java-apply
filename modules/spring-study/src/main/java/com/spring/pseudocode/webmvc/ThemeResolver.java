package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface ThemeResolver
{
    public abstract String resolveThemeName(HttpServletRequest request);

    public abstract void setThemeName(HttpServletRequest request, HttpServletResponse response, String paramString);
}