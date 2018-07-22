package com.spring.pseudocode.webmvc.handle;

import com.spring.pseudocode.webmvc.HandlerExecutionChain;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public abstract class AbstractUrlHandlerMapping extends AbstractHandlerMapping
{
    //处理请求的相对路径为/的Handler
    private Object rootHandler;

    //保存path-->handler的map
    private final Map<String, Object> handlerMap = new LinkedHashMap();

    public Object getRootHandler()
    {
        return this.rootHandler;
    }

    public void setRootHandler(Object rootHandler) {
        this.rootHandler=rootHandler;
    }

    //查找具体的的Handler（执行程序）
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception
    {
        // 根据当前请求获取“查找路径”
        String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
        // 根据路径获取 Handler（即Controller），先尝试直接匹配，再尝试模式匹配
        Object handler = lookupHandler(lookupPath, request);
        if (handler == null)
        {
            Object rawHandler = null;
            if ("/".equals(lookupPath)) {
                rawHandler = getRootHandler();
            }
            if (rawHandler == null) {
                rawHandler = getDefaultHandler();
            }
            if (rawHandler != null)
            {
                if ((rawHandler instanceof String)) {
                    String handlerName = (String)rawHandler;
                    rawHandler = getApplicationContext().getBean(handlerName);
                }
                validateHandler(rawHandler, request);
                handler = buildPathExposingHandler(rawHandler, lookupPath, lookupPath, null);
            }
        }

        return handler;
    }

    //查找handler
    protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception
    {
        Object handler = this.handlerMap.get(urlPath);
        if (handler != null)
        {
            if ((handler instanceof String)) {
                String handlerName = (String)handler;
                handler = getApplicationContext().getBean(handlerName);
            }
            validateHandler(handler, request);
            return buildPathExposingHandler(handler, urlPath, urlPath, null);
        }

        //匹配的handler-path列表
        List<String> matchingPatterns = new ArrayList<String>();
        for (String registeredPattern : this.handlerMap.keySet()) {
            if (getPathMatcher().match(registeredPattern, urlPath)) {

                matchingPatterns.add(registeredPattern);
            }
            //...........
        }

        //对匹配的列表优先级排序，取第一个
        String bestPatternMatch = null;
        Comparator patternComparator = getPathMatcher().getPatternComparator(urlPath);
        if (!matchingPatterns.isEmpty()) {
            Collections.sort(matchingPatterns, patternComparator);
            bestPatternMatch = (String)matchingPatterns.get(0);
        }

        //找到最高优先级的handler-path
        if (bestPatternMatch != null) {
            handler = this.handlerMap.get(bestPatternMatch);
            if (handler == null) {
                handler = this.handlerMap.get(bestPatternMatch.substring(0, bestPatternMatch.length() - 1));
            }

            if ((handler instanceof String)) {
                String handlerName = (String)handler;
                handler = getApplicationContext().getBean(handlerName);
            }
            validateHandler(handler, request);
            String pathWithinMapping = getPathMatcher().extractPathWithinPattern(bestPatternMatch, urlPath);

            Map uriTemplateVariables = new LinkedHashMap();
            for (String matchingPattern : matchingPatterns) {
                if (patternComparator.compare(bestPatternMatch, matchingPattern) == 0) {
                    Map vars = getPathMatcher().extractUriTemplateVariables(matchingPattern, urlPath);
                    Map decodedVars = getUrlPathHelper().decodePathVariables(request, vars);
                    uriTemplateVariables.putAll(decodedVars);
                }
            }
            return buildPathExposingHandler(handler, bestPatternMatch, pathWithinMapping, uriTemplateVariables);
        }

        return null;
    }

    protected void validateHandler(Object handler, HttpServletRequest request) throws Exception
    {

    }

    protected Object buildPathExposingHandler(Object rawHandler, String bestMatchingPattern,
                                              String pathWithinMapping, Map<String, String> uriTemplateVariables)
    {
        HandlerExecutionChain chain = new HandlerExecutionChain(rawHandler);
        return chain;
    }


    //注册handle
    protected void registerHandler(String urlPath, Object handler) throws  IllegalStateException
    {
        Object resolvedHandler = handler;

        if (handler instanceof String) {
            String handlerName = (String)handler;
            if (getApplicationContext().isSingleton(handlerName)) {
                resolvedHandler = getApplicationContext().getBean(handlerName);
            }
        }

        Object mappedHandler = this.handlerMap.get(urlPath);
        if (mappedHandler != null) {
            if (mappedHandler != resolvedHandler)
            {
                throw new IllegalStateException(new StringBuilder().append("Cannot map ").toString());
            }

        }
        else if (urlPath.equals("/")) {
            setRootHandler(resolvedHandler);
        }
        else if (urlPath.equals("/*")) {
            setDefaultHandler(resolvedHandler);
        }
        else {
            this.handlerMap.put(urlPath, resolvedHandler);
        }
    }

    public final Map<String, Object> getHandlerMap()
    {
        return Collections.unmodifiableMap(this.handlerMap);
    }

}
