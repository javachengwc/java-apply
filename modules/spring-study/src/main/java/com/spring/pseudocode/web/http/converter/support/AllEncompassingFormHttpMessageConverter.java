package com.spring.pseudocode.web.http.converter.support;

import com.spring.pseudocode.web.http.converter.FormHttpMessageConverter;
import com.spring.pseudocode.web.http.converter.json.MappingJackson2HttpMessageConverter;
import com.spring.pseudocode.web.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;

public class AllEncompassingFormHttpMessageConverter extends FormHttpMessageConverter {

    private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder",
            AllEncompassingFormHttpMessageConverter.class.getClassLoader());

    private static final boolean jackson2Present = (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper",
            AllEncompassingFormHttpMessageConverter.class.getClassLoader())) &&
            (ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", AllEncompassingFormHttpMessageConverter.class.getClassLoader()));

    private static final boolean jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper",
            AllEncompassingFormHttpMessageConverter.class.getClassLoader());

    private static final boolean gsonPresent = ClassUtils.isPresent("com.google.gson.Gson",
            AllEncompassingFormHttpMessageConverter.class.getClassLoader());

    public AllEncompassingFormHttpMessageConverter()
    {
        addPartConverter(new SourceHttpMessageConverter());

//        if ((jaxb2Present) && (!jackson2Present)) {
//            addPartConverter(new Jaxb2RootElementHttpMessageConverter());
//        }
        if (jackson2Present) {
            addPartConverter(new MappingJackson2HttpMessageConverter());
        }
//        else if (gsonPresent) {
//            addPartConverter(new GsonHttpMessageConverter());
//        }
//
//        if (jackson2XmlPresent) {
//            addPartConverter(new MappingJackson2XmlHttpMessageConverter());
//        }
    }

}
