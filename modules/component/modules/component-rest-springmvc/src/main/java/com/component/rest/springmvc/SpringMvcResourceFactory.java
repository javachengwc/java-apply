package com.component.rest.springmvc;

import com.component.rest.springmvc.util.ClassUtil;
import com.component.rest.springmvc.util.RestTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class SpringMvcResourceFactory  implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(SpringMvcResourceFactory.class);

    private static final String GET="GET";
    private static final String POST="POST";
    private static final String HEAD="HEAD";
    private static final String PUT="PUT";
    private static final String PATCH="PATCH";
    private static final String DELETE="DELETE";
    private static final String OPTIONS="OPTIONS";
    private static final String TRACE="TRACE";

    private static Map<RequestMethod,String> requestMethodMap= new HashMap<RequestMethod,String>();

    private static final MultivaluedMap<String, String> EMPTY_HEADERS   = new MultivaluedHashMap<String, String>();

    private static final List<Class> PARAM_ANNOTATION_CLASSES = Arrays.<Class> asList(
            RequestParam.class,
            PathVariable.class,
            MatrixVariable.class,
            RequestBody.class,
            ModelAttribute.class
            );

    static {
        requestMethodMap.put(RequestMethod.GET,GET);
        requestMethodMap.put(RequestMethod.POST,POST);
        requestMethodMap.put(RequestMethod.HEAD,HEAD);
        requestMethodMap.put(RequestMethod.PUT,PUT);
        requestMethodMap.put(RequestMethod.PATCH,PATCH);
        requestMethodMap.put(RequestMethod.DELETE,DELETE);
        requestMethodMap.put(RequestMethod.OPTIONS,OPTIONS);
        requestMethodMap.put(RequestMethod.TRACE,TRACE);
    }

    private final Class resourceInterface;
    private final String url;
    private final RestTemplate restTemplate;
    private final MultivaluedMap<String, String> headers;
    private final List<Cookie> cookies;
    private final Object entityParam;

    private SpringMvcResourceFactory(final Class resourceInterface,
                                final String url,
                                final RestTemplate restTemplate,
                                final MultivaluedMap<String, String> headers,
                                final List<Cookie> cookies,
                                final Object entityParam) {
        this.resourceInterface = resourceInterface;
        this.url=url;
        this.restTemplate =restTemplate;
        this.headers = headers;
        this.cookies = cookies;
        this.entityParam = entityParam;
    }

    public static <C> C newResource(final Class<C> resourceInterface,
                                    final String url,final RestTemplate restTemplate) {
        String resourceClassName= resourceInterface.getName();
        logger.info("SpringMvcResourceFactory newResource start, resourceClassName={},url={}",resourceClassName,url);
        return newResource(resourceInterface, url,restTemplate, EMPTY_HEADERS,
                Collections.<Cookie> emptyList(), null);
    }

    public static <C> C newResource(final Class<C> resourceInterface, final String url,
                                    final RestTemplate restTemplate,
                                    final MultivaluedMap<String, String> headers,
                                    final List<Cookie> cookies, final Object entityParam) {

        return (C) Proxy.newProxyInstance(
                resourceInterface.getClassLoader(),
                new Class[]{resourceInterface},
                new SpringMvcResourceFactory(resourceInterface,url,restTemplate, headers, cookies, entityParam));
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args)
            throws Throwable {
        if (args == null && method.getName().equals("toString")) {
            return url.toString();
        }

        if (args == null && method.getName().equals("hashCode")) {
            return url.hashCode();
        }
        String methodName = method.getName();
        logger.info("SpringMvcResourceFactory invoke start, methodName={},url={}",methodName,url);
        //resource接口
        final Class<?> interfaceClass = proxy.getClass().getInterfaces()[0];
        //返回结果类型
        final Class<?> responseType = method.getReturnType();

        //获取方法是GET，POST
        String httpMethod = getHttpMethodName(method);
        if (httpMethod == null) {
            for (final Annotation ann : method.getAnnotations()) {
                httpMethod = getHttpMethodName(ann.annotationType());
                if (httpMethod != null) {
                    break;
                }
            }
        }

        String realUrl = addPathFromAnnotation(interfaceClass,url);
        realUrl = addPathFromAnnotation(method,realUrl);
        logger.info("SpringMvcResourceFactory invoke realUrl={},httpMethod={}",realUrl,httpMethod);
        if (httpMethod == null) {
            throw new UnsupportedOperationException("httpMethod is null ");
        }

        final MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<String, String>(this.headers);
        final LinkedList<Cookie> cookies = new LinkedList<Cookie>(this.cookies);
        final Form form = new Form();
        if(this.form!=null) {
            form.asMap().putAll(this.form.asMap());
        }

        final Annotation[][] paramAnns = method.getParameterAnnotations();
        int annsLen = paramAnns==null?0:paramAnns.length;
        logger.info("MvcResourceFactory annsLen length={}",annsLen);
        Object entity = null; //参数值
        Type entityType = null; //参数类型
        Map<String,Object> queryParams= new HashMap<String,Object>();
        Map<String,Object> pathParams = new HashMap<String,Object>();

        for (int i = 0; i < paramAnns.length; i++) {

            //PathParam.class pathParam
            final Map<Class, Annotation> anns = new HashMap<Class, Annotation>();
            for (final Annotation ann : paramAnns[i]) {
                anns.put(ann.annotationType(), ann);
            }
            Annotation ann;
            Class paramClass =method.getParameterTypes()[i];
            String paramName =method.getParameters()[i].getName();
            Object value = args[i];
            logger.info("MvcResourceFactory paramAnns "+i+" ,paramName="+paramName+",value="+value+",paramClass="+paramClass);
            if (!hasAnyParamAnnotation(anns)) {
                //参数没有诸如QueryParam的注解的时候
                if(ClassUtil.isSimpleClass(paramClass)) {
                    //获取不到真正的paramName,需要用spring的实现来获取paramName
                    queryParams.put(paramName,value);
                    logger.info("MvcResourceFactory queryParams add with noting,paramName={},value={}",paramName,value);
                } else if(i==0) {
                    entity = value;
                    entityType = method.getGenericParameterTypes()[i];
                }
            } else {
                if (value == null && (ann = anns.get(DefaultValue.class)) != null) {
                    //通过默认Annotation获取默认值
                    value = ((DefaultValue) ann).value();
                }
                if(value==null) {
                    continue;
                }
                if ((ann = anns.get(PathParam.class)) != null) {
                    pathParams.put(paramName,value);
                    logger.info("MvcResourceFactory pathParams add with PathParam,paramName={},value={}",paramName,value);
                } else if ((ann = anns.get((QueryParam.class))) != null) {
                    paramName=((QueryParam) ann).value();
                    queryParams.put(paramName,value);
                    logger.info("MvcResourceFactory queryParams add with QueryParam,paramName={},value={}",paramName,value);
                } else if ((ann = anns.get((HeaderParam.class))) != null) {
                    if (value instanceof Collection) {
                        for (final Object v : ((Collection) value)) {
                            headers.addAll(((HeaderParam) ann).value(), v.toString());
                        }
                    } else {
                        headers.addAll(((HeaderParam) ann).value(), value.toString());
                    }

                } else if ((ann = anns.get((CookieParam.class))) != null) {
                    final String name = ((CookieParam) ann).value();
                    Cookie c;
                    if (value instanceof Collection) {
                        for (final Object v : ((Collection) value)) {
                            if (!(v instanceof Cookie)) {
                                c = new Cookie(name, v.toString());
                            } else {
                                c = (Cookie) v;
                                if (!name.equals(((Cookie) v).getName())) {
                                    // is this the right thing to do? or should I fail? or ignore the difference?
                                    c = new Cookie(name, c.getValue(), c.getPath(),
                                            c.getDomain(), c.getVersion());
                                }
                            }
                            cookies.add(c);
                        }
                    } else {
                        if (!(value instanceof Cookie)) {
                            cookies.add(new Cookie(name, value.toString()));
                        } else {
                            c = (Cookie) value;
                            if (!name.equals(((Cookie) value).getName())) {
                                // is this the right thing to do? or should I fail? or ignore the difference?
                                cookies.add(new Cookie(name, c.getValue(), c.getPath(), c
                                        .getDomain(), c.getVersion()));
                            }
                        }
                    }
                } else if ((ann = anns.get((MatrixParam.class))) != null) {
                    logger.warn("MvcResourceFactory invoke mvc not support MatrixParam,methodName={},realUrl={}",methodName,realUrl);
                    throw new UnsupportedOperationException("mvc not support MatrixParam");
                } else if ((ann = anns.get((FormParam.class))) != null) {
                    if (value instanceof Collection) {
                        for (final Object v : ((Collection) value)) {
                            form.param(((FormParam) ann).value(), v.toString());
                        }
                    } else {
                        form.param(((FormParam) ann).value(), value.toString());
                    }
                }
                ////参数有诸如QueryParam的注解的解析,结束
            }
        }

        // accepted media types
        String accepts = null;
        Produces produces = method.getAnnotation(Produces.class);
        if (produces == null) {
            produces = interfaceClass.getAnnotation(Produces.class);
        }
        if (produces != null && produces.value().length > 0) {
            accepts = produces.value()[0];
        }
        String contentType = null;
        if (entity != null) {
            List<String> contentTypes= headers.get(HttpHeaders.CONTENT_TYPE);
            if(contentTypes!=null && contentTypes.size()>0) {
                contentType=contentTypes.get(0);
            }
        }
        Consumes consumes = method.getAnnotation(Consumes.class);
        if (consumes == null) {
            consumes = interfaceClass.getAnnotation(Consumes.class);
        }
        if (consumes != null && consumes.value().length > 0) {
            contentType = consumes.value()[0];
        }
        if( contentType==null ) {
            contentType=MediaType.APPLICATION_JSON;
        }
        if(contentType.indexOf("charset")<0) {
            contentType = contentType + "; charset=UTF-8";//默认编码
        }

        headers.addAll(HttpHeaders.CONTENT_TYPE,contentType);
        headers.addAll(HttpHeaders.ACCEPT, accepts);
        MultiValueMap<String,String> toHeaders =new LinkedMultiValueMap<String,String>();
        for(String key:headers.keySet()) {
            toHeaders.put(key,headers.get(key));
        }
        logger.info("MvcResourceFactory invoke....ContentType={},Accept={}",contentType,accepts);

        Object result = null;
        try {
            Type returnType =method.getGenericReturnType();
            Class<?> returnClass =method.getReturnType();
            boolean isParamType=false;
            if(returnType instanceof ParameterizedType) {
                isParamType=true;
                Type entType = ((ParameterizedType)returnType).getActualTypeArguments()[0];
            }
            logger.info("MvcResourceFactory invoke resttemplate begin ,realUrl={}",realUrl);
            result= RestTemplateUtil.request(restTemplate,realUrl,httpMethod,
                    pathParams,queryParams,entity,toHeaders,returnClass,isParamType,returnType);

        }
        catch(Exception e){
            logger.error("MvcResourceFactory invoke restTemplate req error,realUri={}",realUrl,e);
            throw e;
        }
        return result;
    }

    private boolean hasAnyParamAnnotation(final Map<Class, Annotation> anns) {
        for (final Class paramAnnotationClass : PARAM_ANNOTATION_CLASSES) {
            if (anns.containsKey(paramAnnotationClass)) {
                return true;
            }
        }
        return false;
    }

    private static String addPathFromAnnotation(final AnnotatedElement ae, String url) {
        String realUrl=new String(url);
        final Path p = ae.getAnnotation(Path.class);
        if (p != null) {
            String nextPath = p.value();
            if(!nextPath.startsWith("/")) {
                nextPath="/"+nextPath;
            }
            if(realUrl.endsWith("/")) {
                realUrl= realUrl.substring(0,realUrl.length()-1);
            }
            realUrl+=nextPath;
        }
        return realUrl;
    }

    private static String getHttpMethodName(final AnnotatedElement ae) {
        RequestMapping reqMapping = ae.getAnnotation(RequestMapping.class);
        RequestMethod requestMethod = null;
        if(reqMapping!=null && reqMapping.method()!=null) {
            RequestMethod [] requestMethods =reqMapping.method();
            for(RequestMethod per:requestMethods) {
                if(per==RequestMethod.GET || per== RequestMethod.POST) {
                    return requestMethodMap.get(per);
                }
                if(requestMethod==null) {
                    requestMethod=per;
                }
            }
        }
        if(requestMethod==null) {
            return null;
        }
        return requestMethodMap.get(requestMethod);
    }
}