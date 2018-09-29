package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.TypeConverter;
import com.spring.pseudocode.beans.factory.*;
import com.spring.pseudocode.beans.factory.BeanDefinitionStoreException;
import com.spring.pseudocode.beans.factory.BeanFactory;
import com.spring.pseudocode.beans.factory.BeanFactoryAware;
import com.spring.pseudocode.beans.factory.FactoryBean;
import com.spring.pseudocode.beans.factory.NoSuchBeanDefinitionException;
import com.spring.pseudocode.beans.factory.ObjectFactory;
import com.spring.pseudocode.beans.factory.config.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;
import org.springframework.core.OrderComparator;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.ConstructorResolver;
import org.springframework.lang.UsesJava8;
import org.springframework.util.ClassUtils;
import org.springframework.util.CompositeIterator;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable
{
    private static Class<?> javaUtilOptionalClass = null;

    private static Class<?> javaxInjectProviderClass = null;

    private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories;

    private String serializationId;

    //是否允许同名的不同bean definition再次进行注册；
    private boolean allowBeanDefinitionOverriding = true;

    //是否允许eager（相对于lazy）的加载，也就是及时加载
    private boolean allowEagerClassLoading = true;

    private Comparator<Object> dependencyComparator;

    //autowireCandidateResolver是一个策略接口，决定一个特定的bean definition 是否满足做一个特定依赖的自动绑定的候选项
    private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();

    //定义了依赖类型和其对应的自动绑定值的键值对集合
    private final Map<Class<?>, Object> resolvableDependencies = new ConcurrentHashMap(16);

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);

    private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap(64);

    private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap(64);

    private volatile List<String> beanDefinitionNames = new ArrayList(256);

    private volatile Set<String> manualSingletonNames = new LinkedHashSet(16);

    private volatile String[] frozenBeanDefinitionNames;

    private volatile boolean configurationFrozen = false;

    public DefaultListableBeanFactory()
    {
    }

    public DefaultListableBeanFactory(BeanFactory parentBeanFactory)
    {
        super(parentBeanFactory);
    }

    public void setSerializationId(String serializationId)
    {
        if (serializationId != null) {
            serializableFactories.put(serializationId, new WeakReference(this));
        }
        else if (this.serializationId != null) {
            serializableFactories.remove(this.serializationId);
        }
        this.serializationId = serializationId;
    }

    public String getSerializationId()
    {
        return this.serializationId;
    }

    public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding)
    {
        this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
    }

    public boolean isAllowBeanDefinitionOverriding()
    {
        return this.allowBeanDefinitionOverriding;
    }

    public void setAllowEagerClassLoading(boolean allowEagerClassLoading)
    {
        this.allowEagerClassLoading = allowEagerClassLoading;
    }

    public boolean isAllowEagerClassLoading()
    {
        return this.allowEagerClassLoading;
    }

    public void setDependencyComparator(Comparator<Object> dependencyComparator)
    {
        this.dependencyComparator = dependencyComparator;
    }

    public Comparator<Object> getDependencyComparator()
    {
        return this.dependencyComparator;
    }

    public void setAutowireCandidateResolver(AutowireCandidateResolver autowireCandidateResolver)
    {
        if ((autowireCandidateResolver instanceof BeanFactoryAware)) {
                ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory(this);
        }
        this.autowireCandidateResolver = autowireCandidateResolver;
    }

    public AutowireCandidateResolver getAutowireCandidateResolver()
    {
        return this.autowireCandidateResolver;
    }

    public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
    {
        //super.copyConfigurationFrom(otherFactory);
        if ((otherFactory instanceof DefaultListableBeanFactory)) {
            DefaultListableBeanFactory otherListableFactory = (DefaultListableBeanFactory)otherFactory;
            this.allowBeanDefinitionOverriding = otherListableFactory.allowBeanDefinitionOverriding;
            this.allowEagerClassLoading = otherListableFactory.allowEagerClassLoading;
            this.dependencyComparator = otherListableFactory.dependencyComparator;
            setAutowireCandidateResolver((AutowireCandidateResolver) BeanUtils.instantiateClass(getAutowireCandidateResolver().getClass()));
            this.resolvableDependencies.putAll(otherListableFactory.resolvableDependencies);
        }
    }

    public <T> T getBean(Class<T> requiredType) throws BeansException
    {
        return getBean(requiredType, (Object[])null);
    }

    public <T> T getBean(Class<T> requiredType, Object[] args) throws BeansException
    {
//        NamedBeanHolder namedBean = resolveNamedBean(requiredType, args);
//        if (namedBean != null) {
//            return namedBean.getBeanInstance();
//        }
        BeanFactory parent = getParentBeanFactory();
        if (parent != null) {
            return parent.getBean(requiredType, args);
        }
        throw new NoSuchBeanDefinitionException(requiredType);
    }

    @Override
    public boolean isTypeMatch(String paramString, Class<?> paramClass) {
        return false;
    }


    public boolean isTypeMatch(String paramString, ResolvableType type) {
        return false;
    }

    @Override
    public Class<?> getType(String paramString) {
        return null;
    }

    public boolean containsBeanDefinition(String beanName)
    {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    public int getBeanDefinitionCount()
    {
        return this.beanDefinitionMap.size();
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return false;
    }

    public String[] getBeanDefinitionNames()
    {
        if (this.frozenBeanDefinitionNames != null) {
            return (String[])this.frozenBeanDefinitionNames.clone();
        }
        return StringUtils.toStringArray(this.beanDefinitionNames);
    }

    public String[] getBeanNamesForType(ResolvableType type)
    {
        return doGetBeanNamesForType(type, true, true);
    }

    public String[] getBeanNamesForType(Class<?> type)
    {
        return getBeanNamesForType(type, true, true);
    }

    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit)
    {
        if ((!isConfigurationFrozen()) || (type == null) || (!allowEagerInit)) {
            return doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, allowEagerInit);
        }
        Map cache = includeNonSingletons ? this.allBeanNamesByType : this.singletonBeanNamesByType;

        String[] resolvedBeanNames = (String[])cache.get(type);
        if (resolvedBeanNames != null) {
            return resolvedBeanNames;
        }
        resolvedBeanNames = doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, true);
        if (ClassUtils.isCacheSafe(type, getBeanClassLoader())) {
            cache.put(type, resolvedBeanNames);
        }
        return resolvedBeanNames;
    }

    private String[] doGetBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
        List result = new ArrayList();

        for (String beanName : this.beanDefinitionNames)
        {
            if (!isAlias(beanName)) {
                try {
                    RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                    if ((!mbd.isAbstract()) && ((allowEagerInit) || (
                            ((mbd.hasBeanClass()) || (!mbd.isLazyInit()) || (isAllowEagerClassLoading())) &&
                                    (!requiresEagerInitForType(mbd.getFactoryBeanName())))))
                    {
                        boolean isFactoryBean = isFactoryBean(beanName, mbd);
                        BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
                        boolean matchFound = ((allowEagerInit) || (!isFactoryBean) || ((dbd != null) &&
                                (!mbd.isLazyInit())) || (containsSingleton(beanName))) && ((includeNonSingletons) ||
                                (dbd != null ? mbd.isSingleton() : isSingleton(beanName))) && (isTypeMatch(beanName, type));
                        if ((!matchFound) && (isFactoryBean))
                        {
                            beanName = new StringBuilder().append("&").append(beanName).toString();
                            matchFound = ((includeNonSingletons) || (mbd.isSingleton())) && (isTypeMatch(beanName, type));
                        }
                        if (matchFound) {
                            result.add(beanName);
                        }
                    }
                }
                catch (CannotLoadBeanClassException ex)
                {
                    if (allowEagerInit) {
                        throw ex;
                    }
                    onSuppressedException(ex);
                }catch (BeanDefinitionStoreException ex) {
                    if (allowEagerInit) {
                        throw ex;
                    }
                    onSuppressedException(ex);
                }
            }
        }

        for (String beanName : this.manualSingletonNames) {
            try
            {
                if (isFactoryBean(beanName)) {
                    if (((includeNonSingletons) || (isSingleton(beanName))) && (isTypeMatch(beanName, type))) {
                        result.add(beanName);
                        continue;
                    }
                    beanName = new StringBuilder().append("&").append(beanName).toString();
                }
                if (isTypeMatch(beanName, type)) {
                    result.add(beanName);
                }
            }
            catch (NoSuchBeanDefinitionException ex)
            {
            }
        }
        return StringUtils.toStringArray(result);
    }

    private boolean requiresEagerInitForType(String factoryBeanName)
    {
        return (factoryBeanName != null) && (isFactoryBean(factoryBeanName)) && (!containsSingleton(factoryBeanName));
    }

    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException
    {
        return getBeansOfType(type, true, true);
    }

    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException
    {
        String[] beanNames = getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        Map result = new LinkedHashMap(beanNames.length);
        for (String beanName : beanNames) {
            try {
                result.put(beanName, getBean(beanName, type));
            } catch (BeanCreationException ex) {
                Throwable rootCause = ex.getMostSpecificCause();
                if ((rootCause instanceof BeanCurrentlyInCreationException)) {
                    BeanCreationException bce = (BeanCreationException)rootCause;
                    if (isCurrentlyInCreation(bce.getBeanName())) {
                        onSuppressedException(ex);
                        continue;
                    }
                }
                throw ex;
            }
        }
        return result;
    }

    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType)
    {
        List results = new ArrayList();
        for (String beanName : this.beanDefinitionNames) {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            if ((!beanDefinition.isAbstract()) && (findAnnotationOnBean(beanName, annotationType) != null)) {
                results.add(beanName);
            }
        }
        for (String beanName : this.manualSingletonNames) {
            if ((!results.contains(beanName)) && (findAnnotationOnBean(beanName, annotationType) != null)) {
                results.add(beanName);
            }
        }
        return (String[])results.toArray(new String[results.size()]);
    }

    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
    {
        String[] beanNames = getBeanNamesForAnnotation(annotationType);
        Map results = new LinkedHashMap(beanNames.length);
        for (String beanName : beanNames) {
            results.put(beanName, getBean(beanName));
        }
        return results;
    }

    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException
    {
        Annotation ann = null;
        Class beanType = getType(beanName);
        if (beanType != null) {
            ann = AnnotationUtils.findAnnotation(beanType, annotationType);
        }
        if ((ann == null) && (containsBeanDefinition(beanName))) {
            BeanDefinition bd = getMergedBeanDefinition(beanName);
            if ((bd instanceof AbstractBeanDefinition)) {
                AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
                if (abd.hasBeanClass()) {
                    ann = AnnotationUtils.findAnnotation(abd.getBeanClass(), annotationType);
                }
            }
        }
        return (A)ann;
    }

    public void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue)
    {
        if (autowiredValue != null) {
            if ((!(autowiredValue instanceof ObjectFactory)) && (!dependencyType.isInstance(autowiredValue)))
            {
                throw new IllegalArgumentException(new StringBuilder().append("Value [").append(autowiredValue)
                        .append("] does not implement specified dependency type [")
                        .append(dependencyType.getName()).append("]").toString());
            }
            this.resolvableDependencies.put(dependencyType, autowiredValue);
        }
    }

    public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) throws NoSuchBeanDefinitionException
    {
        return isAutowireCandidate(beanName, descriptor, getAutowireCandidateResolver());
    }

    protected boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor,
          AutowireCandidateResolver resolver) throws NoSuchBeanDefinitionException
    {
        String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
        if (containsBeanDefinition(beanDefinitionName)) {
            return isAutowireCandidate(beanName, getMergedLocalBeanDefinition(beanDefinitionName), descriptor, resolver);
        }
        if (containsSingleton(beanName)) {
            return isAutowireCandidate(beanName, new RootBeanDefinition(getType(beanName)), descriptor, resolver);
        }

        BeanFactory parent = getParentBeanFactory();
        if ((parent instanceof DefaultListableBeanFactory))
        {
            return ((DefaultListableBeanFactory)parent).isAutowireCandidate(beanName, descriptor, resolver);
        }
        if ((parent instanceof Serializable))
        {
            //return ((Serializable)parent).isAutowireCandidate(beanName, descriptor);
        }

        return true;
    }

    protected boolean isAutowireCandidate(String beanName, RootBeanDefinition mbd,
          DependencyDescriptor descriptor, AutowireCandidateResolver resolver)
    {
        String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
        //resolveBeanClass(mbd, beanDefinitionName, new Class[0]);
        if (mbd.isFactoryMethodUnique)
        {
            boolean resolve;
            synchronized (mbd.constructorArgumentLock) {
                resolve = mbd.resolvedConstructorOrFactoryMethod == null;
            }
            if (resolve) {
                //new ConstructorResolver(this).resolveFactoryMethodIfPossible(mbd);
            }
        }
        //return resolver.isAutowireCandidate(new BeanDefinitionHolder(mbd, beanName, getAliases(beanDefinitionName)), descriptor);
        return true;
    }

    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException
    {
        BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.get(beanName);
        if (bd == null) {
            throw new NoSuchBeanDefinitionException(beanName);
        }
        return bd;
    }

    public Iterator<String> getBeanNamesIterator()
    {
        CompositeIterator iterator = new CompositeIterator();
        iterator.add(this.beanDefinitionNames.iterator());
        iterator.add(this.manualSingletonNames.iterator());
        return iterator;
    }

    public void clearMetadataCache()
    {
        //super.clearMetadataCache();
        clearByTypeCache();
    }

    public void freezeConfiguration()
    {
        this.configurationFrozen = true;
        this.frozenBeanDefinitionNames = StringUtils.toStringArray(this.beanDefinitionNames);
    }

    public boolean isConfigurationFrozen()
    {
        return this.configurationFrozen;
    }

    protected boolean isBeanEligibleForMetadataCaching(String beanName)
    {
        //return (this.configurationFrozen) || (super.isBeanEligibleForMetadataCaching(beanName));
        return  true;
    }

    public void preInstantiateSingletons() throws BeansException
    {
        List<String> beanNames = new ArrayList(this.beanDefinitionNames);
        for (String beanName : beanNames) {
            RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
            if ((!bd.isAbstract()) && (bd.isSingleton()) && (!bd.isLazyInit())) {
                if (isFactoryBean(beanName)) {
                    FactoryBean factory = (FactoryBean)getBean(new StringBuilder().append("&").append(beanName).toString());
                    boolean isEagerInit=((factory instanceof SmartFactoryBean)) && (((SmartFactoryBean)factory).isEagerInit());
                    if (isEagerInit) {
                        getBean(beanName);
                    }
                }
                else
                {
                    getBean(beanName);
                }
            }

        }

        for (String beanName : beanNames) {
            Object singletonInstance = getSingleton(beanName);
            if ((singletonInstance instanceof SmartInitializingSingleton)) {
                SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton)singletonInstance;
                smartSingleton.afterSingletonsInstantiated();
            }
        }
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException
    {
        if ((beanDefinition instanceof AbstractBeanDefinition)) {
            try {
                ((AbstractBeanDefinition)beanDefinition).validate();
            }
            catch (BeanDefinitionValidationException ex) {
//                throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
//                "Validation of bean definition failed", ex);
            }
        }
        BeanDefinition oldBeanDefinition = (BeanDefinition)this.beanDefinitionMap.get(beanName);
        if (oldBeanDefinition != null) {
            if (!isAllowBeanDefinitionOverriding()) {
//                throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
//                        new StringBuilder().append("Cannot register bean definition [").append(beanDefinition).append("] for bean '")
//                                .append(beanName).append("': There is already [").append(oldBeanDefinition).append("] bound.").toString());
            }
            this.beanDefinitionMap.put(beanName, beanDefinition);
        }
        else {
            if (hasBeanCreationStarted())
            {
                synchronized (this.beanDefinitionMap) {
                    this.beanDefinitionMap.put(beanName, beanDefinition);
                    List updatedDefinitions = new ArrayList(this.beanDefinitionNames.size() + 1);
                    updatedDefinitions.addAll(this.beanDefinitionNames);
                    updatedDefinitions.add(beanName);
                    this.beanDefinitionNames = updatedDefinitions;
                    if (this.manualSingletonNames.contains(beanName)) {
                        Set updatedSingletons = new LinkedHashSet(this.manualSingletonNames);
                        updatedSingletons.remove(beanName);
                        this.manualSingletonNames = updatedSingletons;
                    }
                }
            }
            else
            {
                this.beanDefinitionMap.put(beanName, beanDefinition);
                this.beanDefinitionNames.add(beanName);
                this.manualSingletonNames.remove(beanName);
            }
            this.frozenBeanDefinitionNames = null;
        }

        if ((oldBeanDefinition != null) || (containsSingleton(beanName)))
            resetBeanDefinition(beanName);
    }

    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException
    {

        BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.remove(beanName);
        if (bd == null) {
            throw new NoSuchBeanDefinitionException(beanName);
        }
        if (hasBeanCreationStarted())
        {
            synchronized (this.beanDefinitionMap) {
                List updatedDefinitions = new ArrayList(this.beanDefinitionNames);
                updatedDefinitions.remove(beanName);
                this.beanDefinitionNames = updatedDefinitions;
            }
        }
        else
        {
            this.beanDefinitionNames.remove(beanName);
        }
        this.frozenBeanDefinitionNames = null;
        resetBeanDefinition(beanName);
    }

    protected void resetBeanDefinition(String beanName)
    {
        //clearMergedBeanDefinition(beanName);
        destroySingleton(beanName);
        for (String bdName : this.beanDefinitionNames)
            if (!beanName.equals(bdName)) {
                BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.get(bdName);
//                if (beanName.equals(bd.getParentName()))
//                    resetBeanDefinition(bdName);
            }
    }

    protected boolean allowAliasOverriding()
    {
        return isAllowBeanDefinitionOverriding();
    }

    public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException
    {
        super.registerSingleton(beanName, singletonObject);
        if (hasBeanCreationStarted())
        {
            synchronized (this.beanDefinitionMap) {
                if (!this.beanDefinitionMap.containsKey(beanName)) {
                    Set updatedSingletons = new LinkedHashSet(this.manualSingletonNames.size() + 1);
                    updatedSingletons.addAll(this.manualSingletonNames);
                    updatedSingletons.add(beanName);
                    this.manualSingletonNames = updatedSingletons;
                }
            }
        }
        else if (!this.beanDefinitionMap.containsKey(beanName)) {
            this.manualSingletonNames.add(beanName);
        }
        clearByTypeCache();
    }

    public boolean hasBeanCreationStarted() {
        return false;
    }

    public void destroySingleton(String beanName)
    {
        super.destroySingleton(beanName);
        this.manualSingletonNames.remove(beanName);
        clearByTypeCache();
    }

    @Override
    public void setParentBeanFactory(BeanFactory beanFactory) throws IllegalStateException {

    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {

    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return null;
    }

    @Override
    public void setTempClassLoader(ClassLoader paramClassLoader) {

    }

    @Override
    public ClassLoader getTempClassLoader() {
        return null;
    }

    @Override
    public void setCacheBeanMetadata(boolean paramBoolean) {

    }

    @Override
    public boolean isCacheBeanMetadata() {
        return false;
    }

    @Override
    public String resolveEmbeddedValue(String paramString) {
        return null;
    }

    @Override
    public int getBeanPostProcessorCount() {
        return 0;
    }

    @Override
    public void registerScope(String paramString, Scope paramScope) {

    }

    @Override
    public String[] getRegisteredScopeNames() {
        return new String[0];
    }

    @Override
    public Scope getRegisteredScope(String paramString) {
        return null;
    }

    @Override
    public void setCurrentlyInCreation(String paramString, boolean paramBoolean) {

    }

    @Override
    public boolean isCurrentlyInCreation(String paramString) {
        return false;
    }

    @Override
    public void destroyBean(String paramString, Object paramObject) {

    }

    @Override
    public void destroyScopedBean(String paramString) {

    }

    public void destroySingletons()
    {
        super.destroySingletons();
        this.manualSingletonNames.clear();
        clearByTypeCache();
    }

    private void clearByTypeCache()
    {
        this.allBeanNamesByType.clear();
        this.singletonBeanNamesByType.clear();
    }

    private Object resolveMultipleBeans(DependencyDescriptor descriptor, String beanName,
            Set<String> autowiredBeanNames, TypeConverter typeConverter)
    {
        Class type = descriptor.getDependencyType();
        if (type.isArray()) {
            Class componentType = type.getComponentType();
            ResolvableType resolvableType = descriptor.getResolvableType();
            Class resolvedArrayType = resolvableType.resolve();
            if ((resolvedArrayType != null) && (resolvedArrayType != type)) {
                type = resolvedArrayType;
                componentType = resolvableType.getComponentType().resolve();
            }
            if (componentType == null) {
                return null;
            }
            //Map matchingBeans = findAutowireCandidates(beanName, componentType, new MultiElementDescriptor(descriptor));
            Map matchingBeans =null;
            if (matchingBeans.isEmpty()) {
                return null;
            }
            if (autowiredBeanNames != null) {
                autowiredBeanNames.addAll(matchingBeans.keySet());
            }
            TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
            Object result = converter.convertIfNecessary(matchingBeans.values(), type);
            if ((getDependencyComparator() != null) && ((result instanceof Object[]))) {
                Arrays.sort((Object[])(Object[])result, adaptDependencyComparator(matchingBeans));
            }
            return result;
        }
        if ((Collection.class.isAssignableFrom(type)) && (type.isInterface())) {
            Class elementType = descriptor.getResolvableType().asCollection().resolveGeneric(new int[0]);
            if (elementType == null) {
                return null;
            }
            //Map matchingBeans = findAutowireCandidates(beanName, elementType, new MultiElementDescriptor(descriptor));
            Map matchingBeans =null;
            if (matchingBeans.isEmpty()) {
                return null;
            }
            if (autowiredBeanNames != null) {
                autowiredBeanNames.addAll(matchingBeans.keySet());
            }
            TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
            Object result = converter.convertIfNecessary(matchingBeans.values(), type);
            if ((getDependencyComparator() != null) && ((result instanceof List))) {
                Collections.sort((List)result, adaptDependencyComparator(matchingBeans));
            }
            return result;
        }
        if (Map.class == type) {
            ResolvableType mapType = descriptor.getResolvableType().asMap();
            Class keyType = mapType.resolveGeneric(new int[] { 0 });
            if (String.class != keyType) {
                return null;
            }
            Class valueType = mapType.resolveGeneric(new int[] { 1 });
            if (valueType == null) {
                return null;
            }
            //Map matchingBeans = findAutowireCandidates(beanName, valueType, new MultiElementDescriptor(descriptor));
            Map matchingBeans=null;
            if (matchingBeans.isEmpty()) {
                return null;
            }
            if (autowiredBeanNames != null) {
                autowiredBeanNames.addAll(matchingBeans.keySet());
            }
            return matchingBeans;
        }

        return null;
    }

    private boolean indicatesMultipleBeans(Class<?> type)
    {
        return (type.isArray()) || ((type.isInterface()) &&
                ((Collection.class.isAssignableFrom(type)) || (Map.class.isAssignableFrom(type))));
    }

    private Comparator<Object> adaptDependencyComparator(Map<String, Object> matchingBeans) {
        Comparator comparator = getDependencyComparator();
        if ((comparator instanceof OrderComparator)) {
            return ((OrderComparator)comparator).withSourceProvider(createFactoryAwareOrderSourceProvider(matchingBeans));
        }
        return comparator;
    }

    private FactoryAwareOrderSourceProvider createFactoryAwareOrderSourceProvider(Map<String, Object> beans)
    {
        IdentityHashMap instancesToBeanNames = new IdentityHashMap();
        for (Map.Entry entry : beans.entrySet()) {
            instancesToBeanNames.put(entry.getValue(), entry.getKey());
        }
        return new FactoryAwareOrderSourceProvider(instancesToBeanNames);
    }

    protected String determineAutowireCandidate(Map<String, Object> candidates, DependencyDescriptor descriptor)
    {
        Class requiredType = descriptor.getDependencyType();
        String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
        if (primaryCandidate != null) {
            return primaryCandidate;
        }
        String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
        if (priorityCandidate != null) {
            return priorityCandidate;
        }
        for (Map.Entry entry : candidates.entrySet()) {
            String candidateName = (String)entry.getKey();
            Object beanInstance = entry.getValue();
            if (((beanInstance != null) && (this.resolvableDependencies.containsValue(beanInstance))) ||
                    (matchesBeanName(candidateName, descriptor
                            .getDependencyName()))) {
                return candidateName;
            }
        }
        return null;
    }

    protected String determinePrimaryCandidate(Map<String, Object> candidates, Class<?> requiredType)
    {
        String primaryBeanName = null;
        for (Map.Entry entry : candidates.entrySet()) {
            String candidateBeanName = (String)entry.getKey();
            Object beanInstance = entry.getValue();
            if (isPrimary(candidateBeanName, beanInstance)) {
                if (primaryBeanName != null) {
                    boolean candidateLocal = containsBeanDefinition(candidateBeanName);
                    boolean primaryLocal = containsBeanDefinition(primaryBeanName);
                    if ((candidateLocal) && (primaryLocal))
                    {
                        throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(),
                                new StringBuilder().append("more than one 'primary' bean found among candidates: ").append(candidates.keySet()).toString());
                    }
                    if (candidateLocal)
                        primaryBeanName = candidateBeanName;
                }
                else
                {
                    primaryBeanName = candidateBeanName;
                }
            }
        }
        return primaryBeanName;
    }

    protected String determineHighestPriorityCandidate(Map<String, Object> candidates, Class<?> requiredType)
    {
        String highestPriorityBeanName = null;
        Integer highestPriority = null;
        for (Map.Entry entry : candidates.entrySet()) {
            String candidateBeanName = (String)entry.getKey();
            Object beanInstance = entry.getValue();
            Integer candidatePriority = getPriority(beanInstance);
            if (candidatePriority != null) {
                if (highestPriorityBeanName != null) {
                    if (candidatePriority.equals(highestPriority))
                    {
                        throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(),
                                new StringBuilder().append("Multiple beans found with the same priority ('")
                                .append(highestPriority).append("') among candidates: ").append(candidates.keySet()).toString());
                    }
                    if (candidatePriority.intValue() < highestPriority.intValue()) {
                        highestPriorityBeanName = candidateBeanName;
                        highestPriority = candidatePriority;
                    }
                }
                else {
                    highestPriorityBeanName = candidateBeanName;
                    highestPriority = candidatePriority;
                }
            }
        }
        return highestPriorityBeanName;
    }

    protected boolean isPrimary(String beanName, Object beanInstance)
    {
        if (containsBeanDefinition(beanName)) {
            return getMergedLocalBeanDefinition(beanName).isPrimary();
        }
        BeanFactory parent = getParentBeanFactory();
        return ((parent instanceof DefaultListableBeanFactory)) &&
                (((DefaultListableBeanFactory)parent).isPrimary(beanName, beanInstance));
    }

    protected Integer getPriority(Object beanInstance)
    {
        Comparator comparator = getDependencyComparator();
        if ((comparator instanceof OrderComparator)) {
            return ((OrderComparator)comparator).getPriority(beanInstance);
        }
        return null;
    }

    protected boolean matchesBeanName(String beanName, String candidateName)
    {
        return (candidateName != null) && ((candidateName.equals(beanName)) ||
                (ObjectUtils.containsElement(getAliases(beanName), candidateName)));
    }

    private boolean isSelfReference(String beanName, String candidateName)
    {
        return (beanName != null) && (candidateName != null) && (
                (beanName.equals(candidateName)) || ((containsBeanDefinition(candidateName)) &&
                (beanName.equals(getMergedLocalBeanDefinition(candidateName).getFactoryBeanName()))));
    }

    private void raiseNoMatchingBeanFound(Class<?> type, ResolvableType resolvableType, DependencyDescriptor descriptor) throws BeansException
    {
        checkBeanNotOfRequiredType(type, descriptor);
    }

    private void checkBeanNotOfRequiredType(Class<?> type, DependencyDescriptor descriptor)
    {
        for (String beanName : this.beanDefinitionNames) {
            RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
            Class targetType = mbd.getTargetType();
            if ((targetType != null) && (type.isAssignableFrom(targetType)) &&
                    (isAutowireCandidate(beanName, mbd, descriptor,
                            getAutowireCandidateResolver())))
            {
                Object beanInstance = getSingleton(beanName, false);
                //Class beanType = beanInstance != null ? beanInstance.getClass() : predictBeanType(beanName, mbd, new Class[0]);
                Class beanType =null;
                if (!type.isAssignableFrom(beanType)) {
                    throw new BeanNotOfRequiredTypeException(beanName, type, beanType);
                }
            }
        }

        BeanFactory parent = getParentBeanFactory();
        if ((parent instanceof DefaultListableBeanFactory))
            ((DefaultListableBeanFactory)parent).checkBeanNotOfRequiredType(type, descriptor);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
    {
        throw new NotSerializableException("DefaultListableBeanFactory itself is not deserializable - just a SerializedBeanFactoryReference is");
    }

    protected Object writeReplace() throws ObjectStreamException
    {
        if (this.serializationId != null) {
            return new SerializedBeanFactoryReference(this.serializationId);
        }
        throw new NotSerializableException("DefaultListableBeanFactory has no serialization id");
    }

    static
    {
        try
        {
            javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", DefaultListableBeanFactory.class.getClassLoader());
        }
        catch (ClassNotFoundException localClassNotFoundException1)
        {
        }
        try
        {
            javaxInjectProviderClass = ClassUtils.forName("javax.inject.Provider", DefaultListableBeanFactory.class.getClassLoader());
        }
        catch (ClassNotFoundException localClassNotFoundException2)
        {
        }
        serializableFactories = new ConcurrentHashMap(8);
    }

    @Override
    public BeanFactory getParentBeanFactory() {
        return null;
    }

    @Override
    public boolean containsLocalBean(String paramString) {
        return false;
    }

    private class FactoryAwareOrderSourceProvider implements OrderComparator.OrderSourceProvider
    {
        private final Map<Object, String> instancesToBeanNames;

        public FactoryAwareOrderSourceProvider( Map<Object, String> instancesToBeanNames)
        {
            this.instancesToBeanNames=instancesToBeanNames;
        }

        public Object getOrderSource(Object obj)
        {
            RootBeanDefinition beanDefinition = getRootBeanDefinition((String)this.instancesToBeanNames.get(obj));
            if (beanDefinition == null) {
                return null;
            }
            List sources = new ArrayList(2);
            Method factoryMethod = beanDefinition.getResolvedFactoryMethod();
            if (factoryMethod != null) {
                sources.add(factoryMethod);
            }
            Class targetType = beanDefinition.getTargetType();
            if ((targetType != null) && (targetType != obj.getClass())) {
                sources.add(targetType);
            }
            return sources.toArray(new Object[sources.size()]);
        }

        private RootBeanDefinition getRootBeanDefinition(String beanName) {
            if ((beanName != null) && (DefaultListableBeanFactory.this.containsBeanDefinition(beanName))) {
                BeanDefinition bd = DefaultListableBeanFactory.this.getMergedBeanDefinition(beanName);
                if ((bd instanceof RootBeanDefinition)) {
                    return (RootBeanDefinition)bd;
                }
            }
            return null;
        }
    }

    private static class SerializedBeanFactoryReference implements Serializable
    {
        private final String id;

        public SerializedBeanFactoryReference(String id)
        {
            this.id = id;
        }
        private Object readResolve() {
            Reference ref = (Reference)DefaultListableBeanFactory.serializableFactories.get(this.id);
            if (ref != null) {
                Object result = ref.get();
                if (result != null) {
                    return result;
                }
            }
            return new StaticListableBeanFactory(Collections.emptyMap());
        }
    }
}
