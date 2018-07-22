package com.spring.pseudocode.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public abstract interface View
{

    public abstract String getContentType();

    public abstract void render(Map<String, ?> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
