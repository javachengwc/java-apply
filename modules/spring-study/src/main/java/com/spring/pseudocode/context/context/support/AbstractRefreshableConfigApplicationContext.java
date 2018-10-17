package com.spring.pseudocode.context.context.support;

import com.spring.pseudocode.beans.factory.InitializingBean;
import com.spring.pseudocode.context.context.ApplicationContext;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.util.StringUtils;

public abstract class AbstractRefreshableConfigApplicationContext extends AbstractRefreshableApplicationContext
        implements BeanNameAware, InitializingBean
{
    private String[] configLocations;
    private boolean setIdCalled = false;

    public AbstractRefreshableConfigApplicationContext()
    {
    }

    public AbstractRefreshableConfigApplicationContext(ApplicationContext parent)
    {
        super(parent);
    }

    public void setConfigLocation(String location)
    {
        setConfigLocations(StringUtils.tokenizeToStringArray(location, ",; \t\n"));
    }

    public void setConfigLocations(String[] locations)
    {
        if (locations != null) {
            this.configLocations = new String[locations.length];
            for (int i = 0; i < locations.length; i++)
                this.configLocations[i] = resolvePath(locations[i]).trim();
        }
        else
        {
            this.configLocations = null;
        }
    }

    protected String[] getConfigLocations()
    {
        return this.configLocations != null ? this.configLocations : getDefaultConfigLocations();
    }

    protected String[] getDefaultConfigLocations()
    {
        return null;
    }

    protected String resolvePath(String path)
    {
        //return getEnvironment().resolveRequiredPlaceholders(path);
        return null;
    }

    public void setId(String id)
    {
        //super.setId(id);
        this.setIdCalled = true;
    }

    public void setBeanName(String name)
    {
        if (!this.setIdCalled) {
            //super.setId(name);
            //setDisplayName("ApplicationContext '" + name + "'");
        }
    }

    public void afterPropertiesSet()
    {
        if (!isActive()) {
            //refresh();
        }
    }
}