package com.spring.pseudocode.webmvc.handle;


import com.spring.pseudocode.beans.factory.InitializingBean;
import com.spring.pseudocode.web.method.HandlerMethod;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractHandlerMethodMapping<T> extends AbstractHandlerMapping implements InitializingBean
{
    private final AbstractHandlerMethodMapping<T>.MappingRegistry mappingRegistry = new MappingRegistry();

    private HandlerMethodMappingNamingStrategy<T> namingStrategy;

    public Map<T, HandlerMethod> getHandlerMethods()
    {
        Map localMap = Collections.unmodifiableMap(this.mappingRegistry.getMappings());
        return localMap;
    }

    public void registerMapping(T mapping, Object handler, Method method)
    {
        this.mappingRegistry.register(mapping, handler, method);
    }

    public void unregisterMapping(T mapping)
    {
        this.mappingRegistry.unregister(mapping);
    }


    public HandlerMethodMappingNamingStrategy<T> getNamingStrategy()
    {
        return this.namingStrategy;
    }

    public void afterPropertiesSet()
    {
        initHandlerMethods();
    }

    protected void initHandlerMethods()
    {
//        String[] beanNames = this.detectHandlerMethodsInAncestorContexts ?
//                BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class) :
//                getApplicationContext().getBeanNamesForType(Object.class);

        String[] beanNames = null;
        for (String beanName : beanNames) {
            if (!beanName.startsWith("scopedTarget.")) {
                Class beanType = null;
                try {
                    beanType = getApplicationContext().getType(beanName);
                }
                catch (Throwable ex)
                {

                }
                if ((beanType != null) && (isHandler(beanType))) {
                    detectHandlerMethods(beanName);
                }
            }
        }
        handlerMethodsInitialized(getHandlerMethods());
    }

    protected void detectHandlerMethods(Object handler)
    {
        Class handlerType = (handler instanceof String) ?
                getApplicationContext().getType((String)handler) : handler.getClass();
        Class userType = ClassUtils.getUserClass(handlerType);

//        Map<Method, T> methods = MethodIntrospector.selectMethods(userType, new MethodIntrospector.MetadataLookup(userType)
//        {
//            public T inspect(Method method)
//            {
//                return AbstractHandlerMethodMapping.this.getMappingForMethod(method, this.val$userType);
//            }
//        });
        Map<Method, T> methods =null;
        for (Map.Entry<Method,T> entry : methods.entrySet())
            registerHandlerMethod(handler, (Method)entry.getKey(), entry.getValue());
    }

    protected void registerHandlerMethod(Object handler, Method method, T mapping)
    {
        this.mappingRegistry.register(mapping, handler, method);
    }

    protected HandlerMethod createHandlerMethod(Object handler, Method method)
    {
        HandlerMethod handlerMethod;
        if ((handler instanceof String)) {
            String beanName = (String)handler;

            handlerMethod = new HandlerMethod(beanName,getApplicationContext().getAutowireCapableBeanFactory(), method);
        }
        else {
            handlerMethod = new HandlerMethod(handler, method);
        }
        return handlerMethod;
    }


    protected void handlerMethodsInitialized(Map<T, HandlerMethod> handlerMethods)
    {
    }

    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception
    {
        String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
        HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
        HandlerMethod localObject1 = handlerMethod != null ? handlerMethod.createWithResolvedBean() : null;
        return localObject1;
    }

    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception
    {
        List matches = new ArrayList();
        List directPathMatches = this.mappingRegistry.getMappingsByUrl(lookupPath);
//        if (directPathMatches != null) {
//            addMatchingMappings(directPathMatches, matches, request);
//        }
//        if (matches.isEmpty())
//        {
//            addMatchingMappings(this.mappingRegistry.getMappings().keySet(), matches, request);
//        }

        if (!matches.isEmpty()) {
//            Comparator comparator = new MatchComparator(getMappingComparator(request));
//            Collections.sort(matches, comparator);

            Match bestMatch = (Match)matches.get(0);
            if (matches.size() > 1) {

//                Match secondBestMatch = (Match)matches.get(1);
//                if (comparator.compare(bestMatch, secondBestMatch) == 0) {
//                    Method m1 = bestMatch.handlerMethod.getMethod();
//                    Method m2 = secondBestMatch.handlerMethod.getMethod();
//
//                    throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '" + request
//                            .getRequestURL() + "': {" + m1 + ", " + m2 + "}");
//                }
            }
            //handleMatch(bestMatch.mapping, lookupPath, request);
            return bestMatch.handlerMethod;
        }
        return null;
        //return handleNoMatch(this.mappingRegistry.getMappings().keySet(), lookupPath, request);
    }


    protected abstract boolean isHandler(Class<?> paramClass);

    protected abstract T getMappingForMethod(Method paramMethod, Class<?> paramClass);

    protected abstract Set<String> getMappingPathPatterns(T paramT);

    protected abstract T getMatchingMapping(T paramT, HttpServletRequest paramHttpServletRequest);

    protected abstract Comparator<T> getMappingComparator(HttpServletRequest paramHttpServletRequest);

    private static class EmptyHandler
    {
        public void handle()
        {
            throw new UnsupportedOperationException("not implemented");
        }
    }


    private class Match
    {
        private final T mapping;
        private final HandlerMethod handlerMethod;

        public Match(T mapping, HandlerMethod handlerMethod)
        {
            this.mapping = mapping;
            this.handlerMethod = handlerMethod;
        }

        public String toString()
        {
            return this.mapping.toString();
        }
    }

    private static class MappingRegistration<T>
    {
        private final T mapping;
        private final HandlerMethod handlerMethod;
        private final List<String> directUrls;
        private final String mappingName;

        public MappingRegistration(T mapping, HandlerMethod handlerMethod, List<String> directUrls, String mappingName)
        {
            this.mapping = mapping;
            this.handlerMethod = handlerMethod;
            this.directUrls = (directUrls != null ? directUrls :Collections.EMPTY_LIST);
            this.mappingName = mappingName;
        }

        public T getMapping() {
            return this.mapping;
        }

        public HandlerMethod getHandlerMethod() {
            return this.handlerMethod;
        }

        public List<String> getDirectUrls() {
            return this.directUrls;
        }

        public String getMappingName() {
            return this.mappingName;
        }
    }

    class MappingRegistry
    {
        private final Map<T, AbstractHandlerMethodMapping.MappingRegistration<T>> registry = new HashMap();

        private final Map<T, HandlerMethod> mappingLookup = new LinkedHashMap();

        private final MultiValueMap<String, T> urlLookup = new LinkedMultiValueMap();

        private final Map<String, List<HandlerMethod>> nameLookup = new ConcurrentHashMap();

        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        MappingRegistry()
        {
        }

        public Map<T, HandlerMethod> getMappings() {
            return this.mappingLookup;
        }

        public List<T> getMappingsByUrl(String urlPath)
        {
            return (List)this.urlLookup.get(urlPath);
        }

        public List<HandlerMethod> getHandlerMethodsByMappingName(String mappingName)
        {
            return (List)this.nameLookup.get(mappingName);
        }


        public void register(T mapping, Object handler, Method method) {
            this.readWriteLock.writeLock().lock();
            try {
                HandlerMethod handlerMethod = AbstractHandlerMethodMapping.this.createHandlerMethod(handler, method);
                this.mappingLookup.put(mapping, handlerMethod);

                String name = null;
                if (AbstractHandlerMethodMapping.this.getNamingStrategy() != null) {
                    name = AbstractHandlerMethodMapping.this.getNamingStrategy().getName(handlerMethod, mapping);
                    addMappingName(name, handlerMethod);
                }

                this.registry.put(mapping, new AbstractHandlerMethodMapping.MappingRegistration(mapping, handlerMethod, null, name));
            }
            finally {
                this.readWriteLock.writeLock().unlock();
            }
        }

        private void addMappingName(String name, HandlerMethod handlerMethod) {
            List<HandlerMethod> oldList = (List)this.nameLookup.get(name);
            if (oldList == null) {
                oldList = Collections.emptyList();
            }

            for (HandlerMethod current : oldList) {
                if (handlerMethod.equals(current)) {
                    return;
                }
            }

            List<HandlerMethod>  newList = new ArrayList(oldList.size() + 1);
            ((List)newList).addAll(oldList);
            ((List)newList).add(handlerMethod);
            this.nameLookup.put(name, newList);
        }

        public void unregister(T mapping)
        {
        }


    }
}