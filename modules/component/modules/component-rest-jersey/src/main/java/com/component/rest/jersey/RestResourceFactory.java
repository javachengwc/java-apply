package com.component.rest.jersey;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import com.component.rest.jersey.filter.BalanceClientFilter;
import org.glassfish.jersey.internal.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class RestResourceFactory implements InvocationHandler {

    private static final Logger         logger = LoggerFactory.getLogger(RestResourceFactory.class);
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String[]                       EMPTY                    = {};

    private final WebTarget                             target;
    private final MultivaluedMap<String, Object>        headers;
    private final List<Cookie>                          cookies;
    private final Form                                  form;

    private static final MultivaluedMap<String, Object> EMPTY_HEADERS            = new MultivaluedHashMap<String, Object>();
    private static final Form                           EMPTY_FORM               = new Form();

	private static final List<Class>                    PARAM_ANNOTATION_CLASSES = Arrays
                                                                                     .<Class> asList(
                                                                                             PathParam.class,
                                                                                             QueryParam.class,
                                                                                             HeaderParam.class,
                                                                                             CookieParam.class,
                                                                                             MatrixParam.class,
                                                                                             FormParam.class);

    public static <C> C newResource(final Class<C> resourceInterface, final WebTarget target) {
        return newResource(resourceInterface, target, false, EMPTY_HEADERS,
            Collections.<Cookie> emptyList(), EMPTY_FORM);
    }

    public static <C> C newResource(final Class<C> resourceInterface, final WebTarget target,
                                    final boolean ignoreResourcePath,
                                    final MultivaluedMap<String, Object> headers,
                                    final List<Cookie> cookies, final Form form) {

        return (C) Proxy.newProxyInstance(
            AccessController.doPrivileged(ReflectionHelper.getClassLoaderPA(resourceInterface)),
            new Class[] { resourceInterface },
            new RestResourceFactory(ignoreResourcePath ? target : addPathFromAnnotation(
                resourceInterface, target), headers, cookies, form));
    }

    private RestResourceFactory(final WebTarget target,
                                final MultivaluedMap<String, Object> headers,
                                final List<Cookie> cookies, final Form form) {
        this.target = target;
        this.headers = headers;
        this.cookies = cookies;
        this.form = form;
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args)
                                                                                      throws Throwable {
        if (args == null && method.getName().equals("toString")) {
            return toString();
        }

        if (args == null && method.getName().equals("hashCode")) {
            return hashCode();
        }

        // get the interface describing the resource
        final Class<?> proxyIfc = proxy.getClass().getInterfaces()[0];
        // response type
        final Class<?> responseType = method.getReturnType();

        // determine method name
        String httpMethod = getHttpMethodName(method);
        if (httpMethod == null) {
            for (final Annotation ann : method.getAnnotations()) {
                httpMethod = getHttpMethodName(ann.annotationType());
                if (httpMethod != null) {
                    break;
                }
            }
        }

        // create a new UriBuilder appending the @Path attached to the method
        WebTarget newTarget = addPathFromAnnotation(method, target);
        if (httpMethod == null) {
            if (newTarget == target) {
                // no path annotation on the method -> fail
                throw new UnsupportedOperationException("Not a resource method.");
            } else if (!responseType.isInterface()) {
                // the method is a subresource locator, but returns class,
                // not interface - can't help here
                throw new UnsupportedOperationException("Return type not an interface");
            }
        }

        // process method params (build maps of (Path|Form|Cookie|Matrix|Header..)Params
        // and extract entity type
        final MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<String, Object>(
            this.headers);
        final LinkedList<Cookie> cookies = new LinkedList<Cookie>(this.cookies);
        final Form form = new Form();
        form.asMap().putAll(this.form.asMap());
        final Annotation[][] paramAnns = method.getParameterAnnotations();
        Object entity = null;
        Type entityType = null;
        for (int i = 0; i < paramAnns.length; i++) {
            final Map<Class, Annotation> anns = new HashMap<Class, Annotation>();
            for (final Annotation ann : paramAnns[i]) {
                anns.put(ann.annotationType(), ann);
            }
            Annotation ann;
            Object value = args[i];
            if (!hasAnyParamAnnotation(anns)) {
                entityType = method.getGenericParameterTypes()[i];
                entity = value;
            } else {
                if (value == null && (ann = anns.get(DefaultValue.class)) != null) {
                    value = ((DefaultValue) ann).value();
                }

                if (value != null) {
                    if ((ann = anns.get(PathParam.class)) != null) {
                        newTarget = newTarget.resolveTemplate(((PathParam) ann).value(), value);
                    } else if ((ann = anns.get((QueryParam.class))) != null) {
                        if (value instanceof Collection) {
                            newTarget = newTarget.queryParam(((QueryParam) ann).value(),
                                convert((Collection) value));
                        } else {
                            newTarget = newTarget.queryParam(((QueryParam) ann).value(), value);
                        }
                    } else if ((ann = anns.get((HeaderParam.class))) != null) {
                        if (value instanceof Collection) {
                            headers
                                .addAll(((HeaderParam) ann).value(), convert((Collection) value));
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
                        if (value instanceof Collection) {
                            newTarget = newTarget.matrixParam(((MatrixParam) ann).value(),
                                convert((Collection) value));
                        } else {
                            newTarget = newTarget.matrixParam(((MatrixParam) ann).value(), value);
                        }
                    } else if ((ann = anns.get((FormParam.class))) != null) {
                        if (value instanceof Collection) {
                            for (final Object v : ((Collection) value)) {
                                form.param(((FormParam) ann).value(), v.toString());
                            }
                        } else {
                            form.param(((FormParam) ann).value(), value.toString());
                        }
                    }
                }
            }
        }

        if (httpMethod == null) {
            // the method is a subresource locator
            return RestResourceFactory.newResource(responseType, newTarget, true, headers, cookies, form);
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

        Timer.Context context = null;
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

    private static WebTarget addPathFromAnnotation(final AnnotatedElement ae, WebTarget target) {
        final Path p = ae.getAnnotation(Path.class);
        if (p != null) {
            target = target.path(p.value());
        }
        return target;
    }

    @Override
    public String toString() {
        return target.toString();
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
