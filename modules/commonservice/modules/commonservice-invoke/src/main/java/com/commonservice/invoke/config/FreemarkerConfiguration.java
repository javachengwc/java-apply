package com.commonservice.invoke.config;

import freemarker.template.TemplateModelException;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreemarkerConfiguration {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void configuration() throws TemplateModelException {
        //自定义配置
        this.configuration.setSharedVariable("ctx", this.servletContext.getContextPath());
    }

}
