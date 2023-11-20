package com.velocity.spring5.view;

import java.io.IOException;
import javax.servlet.ServletContext;

import com.velocity.spring5.ui.VelocityEngineFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.web.context.ServletContextAware;

public class VelocityConfigurer extends VelocityEngineFactory
        implements VelocityConfig, InitializingBean, ResourceLoaderAware, ServletContextAware {

    private static final String SPRING_MACRO_RESOURCE_LOADER_NAME = "springMacro";

    private static final String SPRING_MACRO_RESOURCE_LOADER_CLASS = "springMacro.resource.loader.class";

    //此处需要修改
    private static final String SPRING_MACRO_LIBRARY = "com/velocity/spring5/view/spring.vm";

    private VelocityEngine velocityEngine;

    private ServletContext servletContext;

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void afterPropertiesSet() throws IOException, VelocityException {
        if (this.velocityEngine == null) {
            this.velocityEngine = createVelocityEngine();
        }
    }

    @Override
    protected void postProcessVelocityEngine(VelocityEngine velocityEngine) {
        velocityEngine.setApplicationAttribute(ServletContext.class.getName(), this.servletContext);
        velocityEngine.setProperty(
                SPRING_MACRO_RESOURCE_LOADER_CLASS, ClasspathResourceLoader.class.getName());
        velocityEngine.addProperty(
                VelocityEngine.RESOURCE_LOADER, SPRING_MACRO_RESOURCE_LOADER_NAME);
        velocityEngine.addProperty(
                VelocityEngine.VM_LIBRARY, SPRING_MACRO_LIBRARY);

        if (logger.isInfoEnabled()) {
            logger.info("ClasspathResourceLoader with name '" + SPRING_MACRO_RESOURCE_LOADER_NAME +
                    "' added to configured VelocityEngine");
        }
    }

    @Override
    public VelocityEngine getVelocityEngine() {
        return this.velocityEngine;
    }

}
