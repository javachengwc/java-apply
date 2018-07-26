package com.spring.pseudocode.webmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.servlet.http.HttpServlet;
import java.util.HashSet;
import java.util.Set;

public abstract class HttpServletBean extends HttpServlet {

    private static Logger logger= LoggerFactory.getLogger(HttpServletBean.class);

    private final Set<String> requiredProperties = new HashSet();

    private ConfigurableEnvironment environment;

    protected final void addRequiredProperty(String property)
    {
        this.requiredProperties.add(property);
    }

    //init()方法将Servlet初始化参数（init-param）设置到该组件...
    public final void init()
    {

        try
        {
            //......................
            //......................
        }
        catch (Exception ex) {
            this.logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
            //throw ex;
        }

        initServletBean();

        if (this.logger.isDebugEnabled())
            this.logger.debug("Servlet '" + getServletName() + "' configured successfully");
    }

    public final String getServletName()
    {
        return getServletConfig() != null ? getServletConfig().getServletName() : null;
    }

    //提供给子类初始化的扩展点，该方法由FrameworkServlet覆盖
    protected void initServletBean() {

    }

}
