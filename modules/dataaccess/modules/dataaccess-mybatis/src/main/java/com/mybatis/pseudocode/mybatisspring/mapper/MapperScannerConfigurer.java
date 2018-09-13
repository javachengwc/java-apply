package com.mybatis.pseudocode.mybatisspring.mapper;

import com.mybatis.pseudocode.mybatis.session.SqlSessionFactory;
import com.mybatis.pseudocode.mybatisspring.SqlSessionTemplate;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

//<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
//    <property name="basePackage" value="com.shop.dao"/>
//    <property name="sqlSessionTemplateBeanName" value="sqlSessionTemplate"/>
//</bean>
//MapperScannerConfigurer作用是将mapper接口类注册成springbean
//实现BeanDefinitionRegistryPostProcessor接口的类会在Spring初始化Bean定义的时候被调用postProcessBeanDefinitionRegistry方法，用来自定义注册Bean的定义逻辑
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor,
        InitializingBean, ApplicationContextAware, BeanNameAware
{
    //mapper接口类所在的包
    private String basePackage;
    private boolean addToConfig = true;

    private String sqlSessionFactoryBeanName;
    private SqlSessionFactory sqlSessionFactory;

    //sqlSessionTemplate的spring beean名称
    private String sqlSessionTemplateBeanName;
    private SqlSessionTemplate sqlSessionTemplate;

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;
    private ApplicationContext applicationContext;

    private String beanName;
    private boolean processPropertyPlaceHolders;

    //springbean名称生成器
    private BeanNameGenerator nameGenerator;

    public void setBasePackage(String basePackage)
    {
        this.basePackage = basePackage;
    }

    public void setAddToConfig(boolean addToConfig)
    {
        this.addToConfig = addToConfig;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass)
    {
        this.annotationClass = annotationClass;
    }

    public void setMarkerInterface(Class<?> superClass)
    {
        this.markerInterface = superClass;
    }

    @Deprecated
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate)
    {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public void setSqlSessionTemplateBeanName(String sqlSessionTemplateName)
    {
        this.sqlSessionTemplateBeanName = sqlSessionTemplateName;
    }

    @Deprecated
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName)
    {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryName;
    }

    public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders)
    {
        this.processPropertyPlaceHolders = processPropertyPlaceHolders;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    public void setBeanName(String name)
    {
        this.beanName = name;
    }

    public BeanNameGenerator getNameGenerator()
    {
        return this.nameGenerator;
    }

    public void setNameGenerator(BeanNameGenerator nameGenerator)
    {
        this.nameGenerator = nameGenerator;
    }

    public void afterPropertiesSet() throws Exception
    {
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
    {
    }

    //自定义注册Bean的定义逻辑
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {
        if (this.processPropertyPlaceHolders) {
            //设置自己本身的Bean属性
            processPropertyPlaceHolders();
        }

        //定义ClassPathMapperScanner即Mapper扫描器来扫描对应的Mapper文件
        //该扫描器是扫描需要实例化的Bean并把它们加载到BeanDefinitionHolder集合中，以便后续的初始化Bean
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        scanner.setAddToConfig(this.addToConfig);
        scanner.setAnnotationClass(this.annotationClass);
        scanner.setMarkerInterface(this.markerInterface);
//        scanner.setSqlSessionFactory(this.sqlSessionFactory);
//        scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
        scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
        scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
        //设置资源加载器
        scanner.setResourceLoader(this.applicationContext);
        //设置springbean名称生成器
        scanner.setBeanNameGenerator(this.nameGenerator);
        scanner.registerFilters();
        //开始扫描,调用spring中的扫描
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ",; \t\n"));
    }

    private void processPropertyPlaceHolders()
    {
        Map<String,PropertyResourceConfigurer> prcs = this.applicationContext.getBeansOfType(PropertyResourceConfigurer.class);
        if ((!prcs.isEmpty()) && ((this.applicationContext instanceof ConfigurableApplicationContext)))
        {
            BeanDefinition mapperScannerBean = ((ConfigurableApplicationContext)this.applicationContext)
                    .getBeanFactory().getBeanDefinition(this.beanName);

            DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
            factory.registerBeanDefinition(this.beanName, mapperScannerBean);

            for (PropertyResourceConfigurer prc : prcs.values()) {
                prc.postProcessBeanFactory(factory);
            }
            Object values = mapperScannerBean.getPropertyValues();
            this.basePackage = updatePropertyValue("basePackage", (PropertyValues)values);
            this.sqlSessionFactoryBeanName = updatePropertyValue("sqlSessionFactoryBeanName", (PropertyValues)values);
            this.sqlSessionTemplateBeanName = updatePropertyValue("sqlSessionTemplateBeanName", (PropertyValues)values);
        }
    }

    private String updatePropertyValue(String propertyName, PropertyValues values) {
        PropertyValue property = values.getPropertyValue(propertyName);

        if (property == null) {
            return null;
        }

        Object value = property.getValue();

        if (value == null)
            return null;
        if ((value instanceof String))
            return value.toString();
        if ((value instanceof TypedStringValue)) {
            return ((TypedStringValue)value).getValue();
        }
        return null;
    }
}
