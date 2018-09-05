package com.spring.pseudocode.aop.aop.framework;


import com.spring.pseudocode.aop.aop.*;
import com.spring.pseudocode.aop.aop.support.DefaultIntroductionAdvisor;
import com.spring.pseudocode.aop.aop.support.DefaultPointcutAdvisor;
import com.spring.pseudocode.aop.aop.target.EmptyTargetSource;
import com.spring.pseudocode.aop.aop.target.SingletonTargetSource;
import com.spring.pseudocode.aop.aopalliance.aop.Advice;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvisedSupport extends ProxyConfig implements Advised
{
    private static final long serialVersionUID = 2651364800145442165L;
    public static final TargetSource EMPTY_TARGET_SOURCE = null;

    TargetSource targetSource = EMPTY_TARGET_SOURCE;

    private boolean preFiltered = false;

    AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();

    private transient Map<MethodCacheKey, List<Object>> methodCache;
    private List<Class<?>> interfaces = new ArrayList();

    private List<Advisor> advisors = new LinkedList();

    private Advisor[] advisorArray = new Advisor[0];

    public AdvisedSupport()
    {
        initMethodCache();
    }

    public AdvisedSupport(Class<?>[] interfaces)
    {
        this();
        setInterfaces(interfaces);
    }

    private void initMethodCache()
    {
        this.methodCache = new ConcurrentHashMap(32);
    }

    public void setTarget(Object target)
    {
        setTargetSource(new SingletonTargetSource(target));
    }

    public void setTargetSource(TargetSource targetSource)
    {
        this.targetSource = (targetSource != null ? targetSource : EMPTY_TARGET_SOURCE);
    }

    public TargetSource getTargetSource()
    {
        return this.targetSource;
    }

    public void setTargetClass(Class<?> targetClass)
    {
        this.targetSource = EmptyTargetSource.forClass(targetClass);
    }

    public Class<?> getTargetClass()
    {
        return this.targetSource.getTargetClass();
    }

    public void setPreFiltered(boolean preFiltered)
    {
        this.preFiltered = preFiltered;
    }

    public boolean isPreFiltered()
    {
        return this.preFiltered;
    }

    public void setAdvisorChainFactory(AdvisorChainFactory advisorChainFactory)
    {
        this.advisorChainFactory = advisorChainFactory;
    }

    public AdvisorChainFactory getAdvisorChainFactory()
    {
        return this.advisorChainFactory;
    }

    public void setInterfaces(Class<?>[] interfaces)
    {
        this.interfaces.clear();
        for (Class ifc : interfaces)
            addInterface(ifc);
    }

    public void addInterface(Class<?> intf)
    {
        if (!intf.isInterface()) {
            throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
        }
        if (!this.interfaces.contains(intf)) {
            this.interfaces.add(intf);
            adviceChanged();
        }
    }

    public boolean removeInterface(Class<?> intf)
    {
        return this.interfaces.remove(intf);
    }

    public Class<?>[] getProxiedInterfaces()
    {
        return (Class[])this.interfaces.toArray(new Class[this.interfaces.size()]);
    }

    public boolean isInterfaceProxied(Class<?> intf)
    {
        for (Class proxyIntf : this.interfaces) {
            if (intf.isAssignableFrom(proxyIntf)) {
                return true;
            }
        }
        return false;
    }

    public final Advisor[] getAdvisors()
    {
        return this.advisorArray;
    }

    public void addAdvisor(Advisor advisor)
    {
        int pos = this.advisors.size();
        addAdvisor(pos, advisor);
    }

    public void addAdvisor(int pos, Advisor advisor) throws AopConfigException
    {
        if ((advisor instanceof IntroductionAdvisor)) {
            validateIntroductionAdvisor((IntroductionAdvisor)advisor);
        }
        addAdvisorInternal(pos, advisor);
    }

    public boolean removeAdvisor(Advisor advisor)
    {
        int index = indexOf(advisor);
        if (index == -1) {
            return false;
        }

        removeAdvisor(index);
        return true;
    }

    public void removeAdvisor(int index)
            throws AopConfigException
    {
        if (isFrozen()) {
            throw new AopConfigException("Cannot remove Advisor: Configuration is frozen.");
        }
        if ((index < 0) || (index > this.advisors.size() - 1))
        {
            throw new AopConfigException("Advisor index " + index + " is out of bounds: This configuration only has " + this.advisors.size() + " advisors.");
        }

        Advisor advisor = (Advisor)this.advisors.get(index);
        if ((advisor instanceof IntroductionAdvisor)) {
            IntroductionAdvisor ia = (IntroductionAdvisor)advisor;

            for (int j = 0; j < ia.getInterfaces().length; j++) {
                removeInterface(ia.getInterfaces()[j]);
            }
        }

        this.advisors.remove(index);
        updateAdvisorArray();
        adviceChanged();
    }

    public int indexOf(Advisor advisor)
    {
        return this.advisors.indexOf(advisor);
    }

    public boolean replaceAdvisor(Advisor a, Advisor b) throws AopConfigException
    {
        int index = indexOf(a);
        if (index == -1) {
            return false;
        }
        removeAdvisor(index);
        addAdvisor(index, b);
        return true;
    }

    public void addAdvisors(Advisor[] advisors)
    {
        addAdvisors(Arrays.asList(advisors));
    }

    public void addAdvisors(Collection<Advisor> advisors)
    {
        if (isFrozen()) {
            throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
        }
        if (!CollectionUtils.isEmpty(advisors)) {
            for (Advisor advisor : advisors) {
                if ((advisor instanceof IntroductionAdvisor)) {
                    validateIntroductionAdvisor((IntroductionAdvisor)advisor);
                }
                this.advisors.add(advisor);
            }
            updateAdvisorArray();
            adviceChanged();
        }
    }

    private void validateIntroductionAdvisor(IntroductionAdvisor advisor) {
        advisor.validateInterfaces();

        Class[] ifcs = advisor.getInterfaces();
        for (Class ifc : ifcs)
            addInterface(ifc);
    }

    private void addAdvisorInternal(int pos, Advisor advisor) throws AopConfigException
    {
        if (isFrozen()) {
            throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
        }
        if (pos > this.advisors.size())
        {
            throw new IllegalArgumentException("Illegal position " + pos + " in advisor list with size " + this.advisors
                    .size());
        }
        this.advisors.add(pos, advisor);
        updateAdvisorArray();
        adviceChanged();
    }

    protected final void updateAdvisorArray()
    {
        this.advisorArray = ((Advisor[])this.advisors.toArray(new Advisor[this.advisors.size()]));
    }

    protected final List<Advisor> getAdvisorsInternal()
    {
        return this.advisors;
    }

    public void addAdvice(Advice advice) throws AopConfigException
    {
        int pos = this.advisors.size();
        addAdvice(pos, advice);
    }

    public void addAdvice(int pos, Advice advice)  throws AopConfigException
    {
        if ((advice instanceof IntroductionInfo))
        {
            addAdvisor(pos, new DefaultIntroductionAdvisor(advice, (IntroductionInfo)advice));
        } else {
            if ((advice instanceof DynamicIntroductionAdvice))
            {
                throw new AopConfigException("DynamicIntroductionAdvice may only be added as part of IntroductionAdvisor");
            }

            addAdvisor(pos, new DefaultPointcutAdvisor(advice));
        }
    }

    public boolean removeAdvice(Advice advice) throws AopConfigException
    {
        int index = indexOf(advice);
        if (index == -1) {
            return false;
        }

        removeAdvisor(index);
        return true;
    }

    public int indexOf(Advice advice)
    {
        for (int i = 0; i < this.advisors.size(); i++) {
            Advisor advisor = (Advisor)this.advisors.get(i);
            if (advisor.getAdvice() == advice) {
                return i;
            }
        }
        return -1;
    }

    public boolean adviceIncluded(Advice advice)
    {
        if (advice != null) {
            for (Advisor advisor : this.advisors) {
                if (advisor.getAdvice() == advice) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countAdvicesOfType(Class<?> adviceClass)
    {
        int count = 0;
        if (adviceClass != null) {
            for (Advisor advisor : this.advisors) {
                if (adviceClass.isInstance(advisor.getAdvice())) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass)
    {
        MethodCacheKey cacheKey = new MethodCacheKey(method);
        List cached = (List)this.methodCache.get(cacheKey);
        if (cached == null) {
            cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);

            this.methodCache.put(cacheKey, cached);
        }
        return cached;
    }

    protected void adviceChanged()
    {
        this.methodCache.clear();
    }

    protected void copyConfigurationFrom(AdvisedSupport other)
    {
        copyConfigurationFrom(other, other.targetSource, new ArrayList(other.advisors));
    }

    protected void copyConfigurationFrom(AdvisedSupport other, TargetSource targetSource, List<Advisor> advisors)
    {
        copyFrom(other);
        this.targetSource = targetSource;
        this.advisorChainFactory = other.advisorChainFactory;
        this.interfaces = new ArrayList(other.interfaces);
        for (Advisor advisor : advisors) {
            if ((advisor instanceof IntroductionAdvisor)) {
                validateIntroductionAdvisor((IntroductionAdvisor)advisor);
            }
            this.advisors.add(advisor);
        }
        updateAdvisorArray();
        adviceChanged();
    }

    AdvisedSupport getConfigurationOnlyCopy()
    {
        AdvisedSupport copy = new AdvisedSupport();
        copy.copyFrom(this);
        copy.targetSource = EmptyTargetSource.forClass(getTargetClass(), getTargetSource().isStatic());
        copy.advisorChainFactory = this.advisorChainFactory;
        copy.interfaces = this.interfaces;
        copy.advisors = this.advisors;
        copy.updateAdvisorArray();
        return copy;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
    {
        ois.defaultReadObject();
        initMethodCache();
    }

    public String toProxyConfigString()
    {
        return toString();
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(": ").append(this.interfaces.size()).append(" interfaces ");
        sb.append(ClassUtils.classNamesToString(this.interfaces)).append("; ");
        sb.append(this.advisors.size()).append(" advisors ");
        sb.append(this.advisors).append("; ");
        sb.append("targetSource [").append(this.targetSource).append("]; ");
        sb.append(super.toString());
        return sb.toString();
    }

    private static final class MethodCacheKey
            implements Comparable<MethodCacheKey>
    {
        private final Method method;
        private final int hashCode;

        public MethodCacheKey(Method method)
        {
            this.method = method;
            this.hashCode = method.hashCode();
        }

        public boolean equals(Object other)
        {
            return (this == other) || (((other instanceof MethodCacheKey)) && (this.method == ((MethodCacheKey)other).method));
        }

        public int hashCode()
        {
            return this.hashCode;
        }

        public String toString()
        {
            return this.method.toString();
        }

        public int compareTo(MethodCacheKey other)
        {
            int result = this.method.getName().compareTo(other.method.getName());
            if (result == 0) {
                result = this.method.toString().compareTo(other.method.toString());
            }
            return result;
        }
    }
}
