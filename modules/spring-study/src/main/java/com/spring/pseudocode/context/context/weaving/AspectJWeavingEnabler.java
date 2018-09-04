package com.spring.pseudocode.context.context.weaving;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.config.BeanFactoryPostProcessor;
import com.spring.pseudocode.beans.factory.config.ConfigurableListableBeanFactory;
import com.spring.pseudocode.core.core.Ordered;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.instrument.classloading.LoadTimeWeaver;

public class AspectJWeavingEnabler implements BeanFactoryPostProcessor, BeanClassLoaderAware, LoadTimeWeaverAware, Ordered
{
    public static final String ASPECTJ_AOP_XML_RESOURCE = "META-INF/aop.xml";

    private ClassLoader beanClassLoader;

    private LoadTimeWeaver loadTimeWeaver;

    public void setBeanClassLoader(ClassLoader classLoader)
    {
        this.beanClassLoader = classLoader;
    }

    public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver)
    {
        this.loadTimeWeaver = loadTimeWeaver;
    }

    public int getOrder()
    {
        return Integer.MIN_VALUE;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        enableAspectJWeaving(this.loadTimeWeaver, this.beanClassLoader);
    }

    public static void enableAspectJWeaving(LoadTimeWeaver weaverToUse, ClassLoader beanClassLoader)
    {
        if (weaverToUse == null) {
//            if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
//                weaverToUse = new InstrumentationLoadTimeWeaver(beanClassLoader);
//            }
//            else {
//                throw new IllegalStateException("No LoadTimeWeaver available");
//            }
        }
//        weaverToUse.addTransformer(new AspectJClassBypassingClassFileTransformer(new ClassPreProcessorAgentAdapter()));
    }

}
