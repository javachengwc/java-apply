package com.spring.pseudocode.web.web.bind.support;

import com.spring.pseudocode.web.web.bind.WebDataBinder;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;

public abstract interface WebDataBinderFactory
{
    public abstract WebDataBinder createBinder(NativeWebRequest nativeWebRequest, Object obj, String str) throws Exception;
}
