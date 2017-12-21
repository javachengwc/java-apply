package com.component.rest;

import com.codahale.metrics.*;
import com.component.rest.filter.BalanceClientFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.internal.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.security.AccessController;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class MvcResourceFactory implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(MvcResourceFactory.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String[] EMPTY  = {};
    private static final MultivaluedMap<String, Object> EMPTY_HEADERS            = new MultivaluedHashMap<String, Object>();
    private static final Form                           EMPTY_FORM               = new Form();

    private static final List<Class>  PARAM_ANNOTATION_CLASSES = Arrays.<Class> asList(
            PathParam.class,
            QueryParam.class,
            HeaderParam.class,
            CookieParam.class,
            MatrixParam.class,
            FormParam.class);

    private static final String GET="GET";
    private static final String POST="POST";

    private final Class resourceInterface;
    private final String url;
    private final RestTemplate restTemplate;
    private final MultivaluedMap<String, Object> headers;
    private final List<Cookie> cookies;
    private final Form form;

    private MvcResourceFactory(final Class resourceInterface,
                               final String url,
                               final RestTemplate restTemplate,
                               final MultivaluedMap<String, Object> headers,
                               final List<Cookie> cookies,
                               final Form form) {
        this.resourceInterface = resourceInterface;
        this.url=url;
        this.restTemplate =restTemplate;
        this.headers = headers;
        this.cookies = cookies;
        this.form = form;
    }

    public static <C> C newResource(final Class<C> resourceInterface,
                                    final String url,final RestTemplate restTemplate) {
        String resourceClassName= resourceInterface.getName();
        logger.info("MvcResourceFactory newResource start, resourceClassName={},url={}",resourceClassName,url);
        return newResource(resourceInterface, url,restTemplate, EMPTY_HEADERS,
                Collections.<Cookie> emptyList(), EMPTY_FORM);
    }

    public static <C> C newResource(final Class<C> resourceInterface, final String url,
                                    final RestTemplate restTemplate,
                                    final MultivaluedMap<String, Object> headers,
                                    final List<Cookie> cookies, final Form form) {

        return (C) Proxy.newProxyInstance(
                AccessController.doPrivileged(ReflectionHelper.getClassLoaderPA(resourceInterface)),
                new Class[]{resourceInterface},
                new MvcResourceFactory(resourceInterface,url,restTemplate, headers, cookies, form));
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
        logger.info("MvcResourceFactory invoke start, methodName={},url={}",methodName,url);
        //resource接口
        final Class<?> proxyIfc = proxy.getClass().getInterfaces()[0];
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

        String realUrl = addPathFromAnnotation(method,url);
        logger.info("MvcResourceFactory invoke realUrl={},httpMethod={}",realUrl,httpMethod);
        if (httpMethod == null) {
            if (realUrl.equals(url)) {
                // no path annotation on the method -> fail
                throw new UnsupportedOperationException("Not a resource method.");
            } else if (!responseType.isInterface()) {
                throw new UnsupportedOperationException("Return type not an interface");
            }
            throw new UnsupportedOperationException("httpMethod is null ");
        }

        final MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<String, Object>(
                this.headers);
        final LinkedList<Cookie> cookies = new LinkedList<Cookie>(this.cookies);
        final Form form = new Form();
        form.asMap().putAll(this.form.asMap());

        final Annotation[][] paramAnns = method.getParameterAnnotations();
        Object entity = null; //参数值
        Type entityType = null; //参数类型
        Map<String,Object> queryParams= new HashMap<String,Object>();
        for (int i = 0; i < paramAnns.length; i++) {

            //PathParam.class pathParam
            final Map<Class, Annotation> anns = new HashMap<Class, Annotation>();
            for (final Annotation ann : paramAnns[i]) {
                anns.put(ann.annotationType(), ann);
            }
            Annotation ann;
            entityType = method.getGenericParameterTypes()[i];
            String paramName =method.getParameters()[i].getName();
            Object value = args[i];
            if (!hasAnyParamAnnotation(anns)) {
                //参数没有诸如QueryParam的注解的时候
                entity = value;
                if(GET.equalsIgnoreCase(httpMethod)) {
                    queryParams.put(paramName,value);
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
                    logger.warn("MvcResourceFactory invoke mvc not support PathParam,methodName={},realUrl={}",methodName,realUrl);
                    throw new UnsupportedOperationException("mvc not support PathParam");
                } else if ((ann = anns.get((QueryParam.class))) != null) {
                    paramName=((QueryParam) ann).value();
                    queryParams.put(paramName,value);
                } else if ((ann = anns.get((HeaderParam.class))) != null) {
                    if (value instanceof Collection) {
                        headers.addAll(((HeaderParam) ann).value(), convert((Collection) value));
                    } else {
                        headers.addAll(((HeaderParam) ann).value(), value);
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
        Produces produces = method.getAnnotation(Produces.class);
        if (produces == null) {
            produces = proxyIfc.getAnnotation(Produces.class);
        }
        final String[] accepts = (produces == null) ? EMPTY : produces.value();

        // determine content type
        String contentType = null;
        if (entity != null) {
            final List<Object> contentTypeEntries = headers.get(HttpHeaders.CONTENT_TYPE);
            if ((contentTypeEntries != null) && (!contentTypeEntries.isEmpty())) {
                contentType = contentTypeEntries.get(0).toString();
            } else {
                Consumes consumes = method.getAnnotation(Consumes.class);
                if (consumes == null) {
                    consumes = proxyIfc.getAnnotation(Consumes.class);
                }
                if (consumes != null && consumes.value().length > 0) {
                    contentType = consumes.value()[0];
                }
            }
        }

        //TODO:处理到这儿来了
        Invocation.Builder builder = newTarget.request().headers(headers) // this resets all headers so do this first
                .accept(accepts); // if @Produces is defined, propagate values into Accept header; empty array is NO-OP
        if(args != null){
            StringBuffer buf = new StringBuffer();
            for(Object arg : args){
                buf.append(objectMapper.writeValueAsString(arg));
                buf.append(" , ");
            }
            if(buf.length() > 200){
                logger.info("remote rest uri : {} {}  args : {}", httpMethod, newTarget.getUri(), buf.toString().substring(0, 200));
            }else{
                logger.info("remote rest uri : {} {}  args : {}", httpMethod, newTarget.getUri(), buf.toString().substring(0, buf.toString().length() - 2));
            }
        }else{
            logger.info("remote rest uri : {} {} ", httpMethod, newTarget.getUri());
        }
        for (final Cookie c : cookies) {
            builder = builder.cookie(c);
        }

        // Append values to header
        if (proxyIfc != null &&
                !StringUtils.isEmpty(proxyIfc.getName()) &&
                newTarget != null && newTarget.getUri() != null) {
            //过滤器中增加header
            BalanceClientFilter.appendHeaderValues(proxyIfc.getName(), newTarget.getUri().getHost());
        }


        if (entity == null && !form.asMap().isEmpty()) {
            entity = form;
            contentType = MediaType.APPLICATION_FORM_URLENCODED;
        } else {
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM;
            }
            if (!form.asMap().isEmpty()) {
                if (entity instanceof Form) {
                    ((Form) entity).asMap().putAll(form.asMap());
                } else {
                    // TODO: should at least log some warning here
                }
            }
        }

        com.codahale.metrics.Timer.Context context = null;
        Object result = null;

        try {
            GenericType responseGenericType = new GenericType(method.getGenericReturnType());
            if(responseGenericType.getRawType() == Future.class){
                if(responseGenericType.getType() instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType)responseGenericType.getType();
                    responseGenericType = new GenericType(parameterizedType.getActualTypeArguments()[0]);
                }
            }

            if (entity != null) {
                if (entityType instanceof ParameterizedType) {
                    entity = new GenericEntity(entity, entityType);
                }
                result = builder.method(httpMethod, Entity.entity(entity, contentType), responseGenericType);
            } else {
                result = builder.method(httpMethod, responseGenericType);
            }

            if(method.getReturnType() == Future.class){
                result = new SyncFuture(result);
            }
        }
        catch(Exception e){
            throw e;
        }finally{
            if (context != null) {
                context.stop();
            }
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

    private Object[] convert(final Collection value) {
        return value.toArray();
    }

    private static String addPathFromAnnotation(final AnnotatedElement ae, String url) {
        String realUrl=new String(url);
        final Path p = ae.getAnnotation(Path.class);
        if (p != null) {
            String nextPath = p.value();
            if(!nextPath.startsWith("/")) {
                nextPath="/"+nextPath;
            }
            realUrl+=nextPath;
            realUrl= realUrl.replace("//","/");
        }
        return realUrl;
    }

    private static String getHttpMethodName(final AnnotatedElement ae) {
        final HttpMethod a = ae.getAnnotation(HttpMethod.class);
        return a == null ? null : a.value();
    }

    public static class SyncFuture implements Future<Object>{

        private Object obj;

        public SyncFuture(Object obj) {
            this.obj = obj;
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            return true;
        }

        public boolean isCancelled() {
            return false;
        }

        public boolean isDone() {
            return true;
        }

        public Object get() throws InterruptedException, ExecutionException {
            return obj;
        }

        public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return obj;
        }
    }
}
