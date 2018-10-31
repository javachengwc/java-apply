package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//SpringMVC中一套主题对应一个properties文件，里面存放着和当前主题相关的所有资源，比如图片、css样式等。
//ThemeResolver作用是从request解析出主题名，ThemeSource根据主题名找到具体的主题，Theme是ThemeSource找出的一个具体的主题
public abstract interface ThemeResolver
{
    public abstract String resolveThemeName(HttpServletRequest request);

    public abstract void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName);
}