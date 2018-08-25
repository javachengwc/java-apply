package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import com.spring.pseudocode.core.core.io.Resource;
import org.springframework.beans.BeanMetadataAttributeAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.*;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBeanDefinition extends BeanMetadataAttributeAccessor
        implements BeanDefinition, Cloneable
{

    @Deprecated
    public static final int AUTOWIRE_AUTODETECT = 4;
    private volatile Object beanClass;
    private String scope = "";
    private boolean abstractFlag = false;
    private boolean lazyInit = false;
    private int autowireMode = 0;
    private int dependencyCheck = 0;
    private String[] dependsOn;
    private boolean autowireCandidate = true;
    private boolean primary = false;

    private final Map<String, AutowireCandidateQualifier> qualifiers = new LinkedHashMap(0);

    private boolean nonPublicAccessAllowed = true;
    private boolean lenientConstructorResolution = true;
    private String factoryBeanName;
    private String factoryMethodName;
    private ConstructorArgumentValues constructorArgumentValues;
    private MutablePropertyValues propertyValues;
    private MethodOverrides methodOverrides = new MethodOverrides();
    private String initMethodName;
    private String destroyMethodName;
    private boolean enforceInitMethod = true;
    private boolean enforceDestroyMethod = true;
    private boolean synthetic = false;
    private int role = 0;
    private String description;
    private Resource resource;

    protected AbstractBeanDefinition()
    {
        this(null, null);
    }

    protected AbstractBeanDefinition(ConstructorArgumentValues cargs, MutablePropertyValues pvs)
    {
        setConstructorArgumentValues(cargs);
        setPropertyValues(pvs);
    }

    protected AbstractBeanDefinition(BeanDefinition original)
    {
       //...
    }

    public void overrideFrom(BeanDefinition other)
    {
        //...
    }

    public void applyDefaults(BeanDefinitionDefaults defaults)
    {
        setLazyInit(defaults.isLazyInit());
        setAutowireMode(defaults.getAutowireMode());
        setDependencyCheck(defaults.getDependencyCheck());
        setInitMethodName(defaults.getInitMethodName());
        setEnforceInitMethod(false);
        setDestroyMethodName(defaults.getDestroyMethodName());
        setEnforceDestroyMethod(false);
    }

    public void setBeanClassName(String beanClassName)
    {
        this.beanClass = beanClassName;
    }

    public String getBeanClassName()
    {
        Object beanClassObject = this.beanClass;
        if ((beanClassObject instanceof Class)) {
            return ((Class)beanClassObject).getName();
        }

        return (String)beanClassObject;
    }

    public void setBeanClass(Class<?> beanClass)
    {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() throws IllegalStateException
    {
        Object beanClassObject = this.beanClass;
        //...
        return (Class)beanClassObject;
    }

    public boolean hasBeanClass()
    {
        return this.beanClass instanceof Class;
    }

    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException
    {
        String className = getBeanClassName();
        if (className == null) {
            return null;
        }
        Class resolvedClass = ClassUtils.forName(className, classLoader);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getScope()
    {
        return this.scope;
    }

    public boolean isSingleton()
    {
        return ("singleton".equals(this.scope)) || ("".equals(this.scope));
    }

    public boolean isPrototype()
    {
        return "prototype".equals(this.scope);
    }

    public void setAbstract(boolean abstractFlag)
    {
        this.abstractFlag = abstractFlag;
    }

    public boolean isAbstract()
    {
        return this.abstractFlag;
    }

    public void setLazyInit(boolean lazyInit)
    {
        this.lazyInit = lazyInit;
    }

    public boolean isLazyInit()
    {
        return this.lazyInit;
    }

    public void setAutowireMode(int autowireMode)
    {
        this.autowireMode = autowireMode;
    }

    public int getAutowireMode()
    {
        return this.autowireMode;
    }

    public int getResolvedAutowireMode()
    {
        if (this.autowireMode == 4)
        {
            Constructor[] constructors = getBeanClass().getConstructors();
            for (Constructor constructor : constructors) {
                if (constructor.getParameterTypes().length == 0) {
                    return 2;
                }
            }
            return 3;
        }

        return this.autowireMode;
    }

    public void setDependencyCheck(int dependencyCheck)
    {
        this.dependencyCheck = dependencyCheck;
    }

    public int getDependencyCheck()
    {
        return this.dependencyCheck;
    }

    public void setDependsOn(String[] dependsOn)
    {
        this.dependsOn = dependsOn;
    }

    public String[] getDependsOn()
    {
        return this.dependsOn;
    }

    public void setAutowireCandidate(boolean autowireCandidate)
    {
        this.autowireCandidate = autowireCandidate;
    }

    public boolean isAutowireCandidate()
    {
        return this.autowireCandidate;
    }

    public void setPrimary(boolean primary)
    {
        this.primary = primary;
    }

    public boolean isPrimary()
    {
        return this.primary;
    }

    public void addQualifier(AutowireCandidateQualifier qualifier)
    {
        this.qualifiers.put(qualifier.getTypeName(), qualifier);
    }

    public boolean hasQualifier(String typeName)
    {
        return this.qualifiers.keySet().contains(typeName);
    }

    public AutowireCandidateQualifier getQualifier(String typeName)
    {
        return (AutowireCandidateQualifier)this.qualifiers.get(typeName);
    }

    public Set<AutowireCandidateQualifier> getQualifiers()
    {
        return new LinkedHashSet(this.qualifiers.values());
    }

    public void copyQualifiersFrom(AbstractBeanDefinition source)
    {
        this.qualifiers.putAll(source.qualifiers);
    }

    public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed)
    {
        this.nonPublicAccessAllowed = nonPublicAccessAllowed;
    }

    public boolean isNonPublicAccessAllowed()
    {
        return this.nonPublicAccessAllowed;
    }

    public void setLenientConstructorResolution(boolean lenientConstructorResolution)
    {
        this.lenientConstructorResolution = lenientConstructorResolution;
    }

    public boolean isLenientConstructorResolution()
    {
        return this.lenientConstructorResolution;
    }

    public void setFactoryBeanName(String factoryBeanName)
    {
        this.factoryBeanName = factoryBeanName;
    }

    public String getFactoryBeanName()
    {
        return this.factoryBeanName;
    }

    public void setFactoryMethodName(String factoryMethodName)
    {
        this.factoryMethodName = factoryMethodName;
    }

    public String getFactoryMethodName()
    {
        return this.factoryMethodName;
    }

    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues)
    {
        this.constructorArgumentValues = (constructorArgumentValues != null ? constructorArgumentValues : new ConstructorArgumentValues());
    }

    public ConstructorArgumentValues getConstructorArgumentValues()
    {
        return this.constructorArgumentValues;
    }

    public boolean hasConstructorArgumentValues()
    {
        return !this.constructorArgumentValues.isEmpty();
    }

    public void setPropertyValues(MutablePropertyValues propertyValues)
    {
        this.propertyValues = (propertyValues != null ? propertyValues : new MutablePropertyValues());
    }

    public MutablePropertyValues getPropertyValues()
    {
        return this.propertyValues;
    }

    public void setMethodOverrides(MethodOverrides methodOverrides)
    {
        this.methodOverrides = (methodOverrides != null ? methodOverrides : new MethodOverrides());
    }

    public MethodOverrides getMethodOverrides()
    {
        return this.methodOverrides;
    }

    public void setInitMethodName(String initMethodName)
    {
        this.initMethodName = initMethodName;
    }

    public String getInitMethodName()
    {
        return this.initMethodName;
    }

    public void setEnforceInitMethod(boolean enforceInitMethod)
    {
        this.enforceInitMethod = enforceInitMethod;
    }

    public boolean isEnforceInitMethod()
    {
        return this.enforceInitMethod;
    }

    public void setDestroyMethodName(String destroyMethodName)
    {
        this.destroyMethodName = destroyMethodName;
    }

    public String getDestroyMethodName()
    {
        return this.destroyMethodName;
    }

    public void setEnforceDestroyMethod(boolean enforceDestroyMethod)
    {
        this.enforceDestroyMethod = enforceDestroyMethod;
    }

    public boolean isEnforceDestroyMethod()
    {
        return this.enforceDestroyMethod;
    }

    public void setSynthetic(boolean synthetic)
    {
        this.synthetic = synthetic;
    }

    public boolean isSynthetic()
    {
        return this.synthetic;
    }

    public void setRole(int role)
    {
        this.role = role;
    }

    public int getRole()
    {
        return this.role;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setResource(Resource resource)
    {
        this.resource = resource;
    }

    public Resource getResource()
    {
        return this.resource;
    }

    public BeanDefinition getOriginatingBeanDefinition()
    {
        return null;
    }

    public void validate() throws BeanDefinitionValidationException
    {
        //...
    }

    public void prepareMethodOverrides()throws BeanDefinitionValidationException
    {
        MethodOverrides methodOverrides = getMethodOverrides();
        if (!methodOverrides.isEmpty()) {
            Set<MethodOverride> overrides = methodOverrides.getOverrides();
            synchronized (overrides) {
                for (MethodOverride mo : overrides)
                    prepareMethodOverride(mo);
            }
        }
    }

    protected void prepareMethodOverride(MethodOverride mo) throws BeanDefinitionValidationException
    {
        int count = ClassUtils.getMethodCountForName(getBeanClass(), mo.getMethodName());
        if (count == 0)
        {
            throw new BeanDefinitionValidationException(new StringBuilder().append("Invalid method override: no method with name '")
                    .append(mo.getMethodName()).append("' on class [")
                    .append(getBeanClassName()).append("]").toString());
        }
        if (count == 1)
        {
           // mo.setOverloaded(false);
        }
    }

    public Object clone()
    {
        return cloneBeanDefinition();
    }

    public abstract AbstractBeanDefinition cloneBeanDefinition();

}
