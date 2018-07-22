package com.spring.pseudocode.web.multipart;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public abstract interface MultipartResolver
{
    public abstract boolean isMultipart(HttpServletRequest request);

    public abstract MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException;

    public abstract void cleanupMultipart(MultipartHttpServletRequest request);
}
