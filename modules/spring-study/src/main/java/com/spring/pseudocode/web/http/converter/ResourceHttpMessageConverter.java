package com.spring.pseudocode.web.http.converter;

import com.spring.pseudocode.core.core.io.Resource;
import com.spring.pseudocode.web.http.MediaType;
import org.springframework.util.ClassUtils;

public class ResourceHttpMessageConverter extends AbstractHttpMessageConverter<Resource>
{
    private static final boolean jafPresent = ClassUtils.isPresent("javax.activation.FileTypeMap", ResourceHttpMessageConverter.class.getClassLoader());

    public ResourceHttpMessageConverter()
    {
        super(MediaType.ALL);
    }

    protected boolean supports(Class<?> clazz)
    {
        return Resource.class.isAssignableFrom(clazz);
    }

}
