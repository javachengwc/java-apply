package com.mybatis.pseudocode.mybatisspring.mapper;

import com.mybatis.pseudocode.mybatis.session.SqlSessionFactory;
import com.mybatis.pseudocode.mybatisspring.SqlSessionTemplate;
import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

//ClassPathMapperScanner扫描器继承了Spring中的ClassPathBeanDefinitionScanner，
//该扫描器是扫描需要实例化的Bean并把它们加载到BeanDefinitionHolder集合中，以便后续的初始化Bean
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner
{
    private boolean addToConfig = true;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;
    private String sqlSessionTemplateBeanName;
    private String sqlSessionFactoryBeanName;
    private Class<? extends Annotation> annotationClass;
    private Class<?> markerInterface;
    private MapperFactoryBean<?> mapperFactoryBean = new MapperFactoryBean();

    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public void setSqlSessionTemplateBeanName(String sqlSessionTemplateBeanName) {
        this.sqlSessionTemplateBeanName = sqlSessionTemplateBeanName;
    }

    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
    }

    public void setMapperFactoryBean(MapperFactoryBean<?> mapperFactoryBean) {
        this.mapperFactoryBean = (mapperFactoryBean != null ? mapperFactoryBean : new MapperFactoryBean());
    }

    public void registerFilters()
    {
        boolean acceptAllInterfaces = true;

        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        if (this.markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface)
            {
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }
        //...
    }

    //具体扫描
    public Set<BeanDefinitionHolder> doScan(String[] basePackages)
    {
        //调用父类搜索将会搜索找出所有候选人
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty())
            this.logger.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        else {
            //此方法设置配置属性
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    //对候选人设置属性
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions)
    {
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition)holder.getBeanDefinition();

            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + definition
                        .getBeanClassName() + "' mapperInterface");
            }

            //mapper接口是生成的bean的原始类
            //实际上该bean类型是MapperFactoryBean
            //设置构造函数参数为该bean的原始类名(即Mapper接口的名称)
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());

            //设置beanClass为MapperFactoryBean
            definition.setBeanClass(this.mapperFactoryBean.getClass());

            //设置addToConfig属性
            definition.getPropertyValues().add("addToConfig", Boolean.valueOf(this.addToConfig));

            //配置sqlSessionFactory
            boolean explicitFactoryUsed = false;
            if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
                definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
                explicitFactoryUsed = true;
            } else if (this.sqlSessionFactory != null) {
                definition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
                explicitFactoryUsed = true;
            }

            //配置sqlSessionTemplate
            if (StringUtils.hasText(this.sqlSessionTemplateBeanName)) {
                if (explicitFactoryUsed) {
                    this.logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
                }
                definition.getPropertyValues().add("sqlSessionTemplate", new RuntimeBeanReference(this.sqlSessionTemplateBeanName));
                explicitFactoryUsed = true;
            } else if (this.sqlSessionTemplate != null) {
                if (explicitFactoryUsed) {
                    this.logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
                }
                definition.getPropertyValues().add("sqlSessionTemplate", this.sqlSessionTemplate);
                explicitFactoryUsed = true;
            }

            if (!explicitFactoryUsed) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
                }
                definition.setAutowireMode(2);
            }
            //设置完MapperFactoryBean的beanDefinition信息之后，Spring在初始化Bean时就会初始化这个Bean，
        }
    }

    //判断是否候选人
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition)
    {
        //必须是接口，且是独立接口 才满足是候选人
        return (beanDefinition.getMetadata().isInterface()) && (beanDefinition.getMetadata().isIndependent());
    }

    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition)
    {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        }
        this.logger.warn("Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition
                .getBeanClassName() + "' mapperInterface. Bean already defined with the same name!");
        return false;
    }
}
