package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

//LocalResolver用于从request中解析出Locale。
//用到LocaleResolver有两处：一是ViewResolver解析视图的时候；二是使用到国际化资源或者主题的时候，
//国际化资源或者主题主要使用RequestContext的getMessage()和getThemeMessage()方法
public abstract interface LocaleResolver
{
    public abstract Locale resolveLocale(HttpServletRequest request);

    public abstract void setLocale(HttpServletRequest request, HttpServletResponse response, Locale paramLocale);
}
