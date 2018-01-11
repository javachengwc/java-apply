package com.component.rest.springmvc;

import com.component.rest.springmvc.util.ClassUtil;
import com.component.rest.springmvc.util.RestTemplateUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpringMvcResourceFactory  implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(SpringMvcResourceFactory.class);

    private static final String GET="GET";
    private static final String POST="POST";
    private static final String HEAD="HEAD";
    private static final String PUT="PUT";
    private static final String PATCH="PATCH"; //表示部分更新，把它当更新就行了
    private static final String DELETE="DELETE";
    private static final String OPTIONS="OPTIONS";
    private static final String TRACE="TRACE";

    private static String PATH_URL_PATTERN="\\{(.*?)\\}";

    private static Map<RequestMethod,String> requestMethodMap= new HashMap<RequestMethod,String>();

    //参数注解
    private static final List<Class> PARAM_ANNOTATION_CLASSES = Arrays.<Class> asList(
        RequestParam.class,
        PathVariable.class,
        MatrixVariable.class, //类似Map<String,List<String>>这样的参数绑定,可以从url中提取，暂不支持
        RequestBody.class,
        ModelAttribute.class  //绑定对象，把它当一个对象传参就行
    );

    //方法注解
    private static final List<Class> METHOD_ANNOTATION_CLASSES = Arrays.<Class>asList(
        RequestMapping.class,
        GetMapping.class,
        PutMapping.class,
        PostMapping.class,
        DeleteMapping.class,
        PatchMapping.class
    );

    private static ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

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

    private Class resourceInterface;
    private String url;
    private RestTemplate restTemplate;

    private SpringMvcResourceFactory(final Class resourceInterface,
                                final String url,
                                final RestTemplate restTemplate) {
        this.resourceInterface = resourceInterface;
        this.url=url;
        this.restTemplate =restTemplate;
    }

    public static <C> C newResource(final Class<C> resourceInterface,
                                    final String url,final RestTemplate restTemplate) {
        String resourceClassName= resourceInterface.getName();
        logger.info("SpringMvcResourceFactory newResource start, resourceClassName={},url={}",resourceClassName,url);
        return (C) Proxy.newProxyInstance(
                resourceInterface.getClassLoader(),
                new Class[]{resourceInterface},
                new SpringMvcResourceFactory(resourceInterface,url,restTemplate));
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

        //根据类，方法组装出url
        String realUrl = addPathFromClassAnnotation(interfaceClass,url);
        realUrl = addPathFromMethodAnnotation(method,realUrl);
        logger.info("SpringMvcResourceFactory invoke realUrl={},httpMethod={}",realUrl,httpMethod);

        if (httpMethod == null) {
            throw new UnsupportedOperationException("httpMethod is null ");
        }

        List<String> pathNameList=getUrlPathNames(realUrl);

        //提取的数据
        MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<String, String>();
        LinkedList<Cookie> cookies = new LinkedList<Cookie>();
        Object bodyEntity=null; //body参
        Object entity = null; //实体参
        Map<String,Object> queryParams= new HashMap<String,Object>();//查询参数
        Map<String,Object> pathParams = new HashMap<String,Object>();//路径参数
        String contentType = null;

        Annotation [] methodAnns = method.getDeclaredAnnotations();
        final Annotation[][] paramAnns = method.getParameterAnnotations();
        int paramAnnsLen = paramAnns==null?0:paramAnns.length;

        if(methodAnns!=null && methodAnns.length>0) {
            if(Arrays.asList(methodAnns).contains(ResponseBody.class)) {
                contentType=MediaType.APPLICATION_JSON;
            }
        }
        logger.info("SpringMvcResourceFactory paramAnnsLen length={}",paramAnnsLen);
        int entityIndex=0;
        for (int i = 0; i < paramAnns.length; i++) {
            logger.info("SpringMvcResourceFactory invoke deal param, paramIndex={}",i);
            final Map<Class, Annotation> anns = new HashMap<Class, Annotation>();
            for (final Annotation ann : paramAnns[i]) {
                anns.put(ann.annotationType(), ann);
            }
            Annotation ann;
            Class paramClass =method.getParameterTypes()[i];
            String paramName =parameterNameDiscoverer.getParameterNames(method)[i];
            Object value = args[i];

            logger.info("SpringMvcResourceFactory paramAnns "+i+" ,paramName="+paramName+",value="+value+",paramClass="+paramClass);
            if (!hasAnyParamAnnotation(anns)) {
                //方法参数没带注释
                if(ClassUtil.isSimpleClass(paramClass)) {
                    queryParams.put(paramName,value);
                    logger.info("SpringMvcResourceFactory queryParams add with noting,paramName={},value={}",paramName,value);
                } else {
                    if(paramClass==HttpServletRequest.class || paramClass== HttpServletResponse.class
                            || paramClass==HttpSession.class || paramClass ==Errors.class) {
                        continue;
                    }
                    if(entityIndex==0) {
                        entity = value;
                    }
                    entityIndex++;
                }
            } else {
                //方法参数带了注解
                if(value==null) {
                    continue;
                }
                if ((ann = anns.get(PathVariable.class)) != null) {
                    String pathName =((PathVariable) ann).value();
                    if(StringUtils.isBlank(pathName)) {
                        pathName=paramName;
                    }
                    pathParams.put(pathName,value);
                    logger.info("SpringMvcResourceFactory pathParams add with PathVariable,pathName={},value={}",pathName,value);
                } else if ((ann = anns.get((RequestParam.class))) != null) {
                    String queryName=((RequestParam) ann).value();
                    if(StringUtils.isBlank(queryName)) {
                        queryName=paramName;
                    }
                    queryParams.put(queryName,value);
                    logger.info("SpringMvcResourceFactory queryParams add with RequestParam,queryName={},value={}",queryName,value);
                } else if ((ann = anns.get((RequestBody.class))) != null) {
                    contentType=MediaType.APPLICATION_JSON;
                    bodyEntity = value;
                } else if ((ann = anns.get((MatrixVariable.class))) != null) {
                    logger.warn("SpringMvcResourceFactory invoke  not support MatrixVariable,methodName={},realUrl={}",methodName,realUrl);
                    throw new UnsupportedOperationException("not support MatrixVariable");
                } else if ((ann = anns.get((ModelAttribute.class))) != null) {
                    logger.info("SpringMvcResourceFactory find ModelAttribute,paramName={},value={}",paramName,value);
                    if(ClassUtil.isSimpleClass(paramClass)) {
                        continue;
                    }
                    Map<String,Object> valueMap = obj2Map(value);
                    for(Map.Entry<String,Object> per:valueMap.entrySet()) {
                        Object curValue=per.getValue();
                        if(curValue==null) {
                            continue;
                        }
                        String key =per.getKey();
                        if(pathNameList!=null && pathNameList.contains(key)) {
                            pathParams.put(key,curValue);
                        } else {
                            queryParams.put(key,curValue);
                        }
                    }
                }
                ////参数有诸如RequestParam的注解的解析,结束
            }
        }

        //处理entity
        if(entity!=null) {
             Map<String,Object> entityMap = obj2Map(entity);
             for(Map.Entry<String,Object> per:entityMap.entrySet()) {
                 Object value=per.getValue();
                 if(value==null) {
                     continue;
                 }
                 String key =per.getKey();
                 queryParams.put(key,value);
             }
        }

        //把解析出来的queryParams,pathParams,bodyEntity打印出来
        if(logger.isDebugEnabled()) {
            logger.debug("SpringMvcResourceFactory invoke parse end, realUrl={},queryParams={},pathParams={},bodyEntity={}",
                    realUrl,map2Str(queryParams),map2Str(pathParams),bodyEntity);
        }

        //处理contentType
        if( contentType==null ) {
            contentType=MediaType.APPLICATION_JSON;
        }
//        if(contentType.indexOf("charset")<0) {
//            contentType = contentType + "; charset=UTF-8";//默认编码
//        }

        //处理header
        headers.addAll(HttpHeaders.CONTENT_TYPE,contentType);
        headers.addAll(HttpHeaders.ACCEPT, "*");
        MultiValueMap<String,String> toHeaders =new LinkedMultiValueMap<String,String>();
        for(String key:headers.keySet()) {
            toHeaders.put(key,headers.get(key));
        }

        Object result = null;
        try {
            Type returnType =method.getGenericReturnType();
            Class<?> returnClass =method.getReturnType();
            boolean isParamType=false;
            if(returnType instanceof ParameterizedType) {
                isParamType=true;
                Type entType = ((ParameterizedType)returnType).getActualTypeArguments()[0];
            }
            logger.info("SpringMvcResourceFactory invoke restTemplate begin ,realUrl={}",realUrl);
            result= RestTemplateUtil.request(restTemplate,realUrl,httpMethod,
                    pathParams,queryParams,bodyEntity,toHeaders,returnClass,isParamType,returnType);

        }
        catch(Exception e){
            logger.error("SpringMvcResourceFactory invoke restTemplate req error,realUri={}",realUrl,e);
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

    private static String addPathFromClassAnnotation(final AnnotatedElement ae, String url) {
        String realUrl=new String(url);
        final RequestMapping p = ae.getAnnotation(RequestMapping.class);
        if (p != null) {
            String nextPaths [] = p.value();
            if(nextPaths==null || nextPaths.length<=0) {
                nextPaths = p.path();
            }
            if(nextPaths==null || nextPaths.length<=0) {
                return realUrl;
            }
            String nextPath= nextPaths[0];
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

    private static String addPathFromMethodAnnotation(final AnnotatedElement ae, String url) {
        String realUrl=new String(url);
        Annotation [] ans= ae.getDeclaredAnnotations();
        if(ans==null || ans.length<=0) {
           return realUrl;
        }
        String nextPath=null;
        for(Annotation an:ans) {
            Class anType = an.annotationType();
            if(!METHOD_ANNOTATION_CLASSES.contains(anType)) {
                continue;
            }
            if(anType== RequestMapping.class) {
                RequestMapping p = (RequestMapping)an;
                nextPath=getAnPath(p);
            }
            if(anType== GetMapping.class) {
                GetMapping p = (GetMapping)an;
                nextPath=getAnPath(p);
            }
            if(anType== PostMapping.class) {
                PostMapping p = (PostMapping)an;
                nextPath=getAnPath(p);
            }
            if(anType== PutMapping.class) {
                PutMapping p = (PutMapping)an;
                nextPath=getAnPath(p);
            }
            if(anType== DeleteMapping.class) {
                DeleteMapping p = (DeleteMapping)an;
                nextPath=getAnPath(p);
            }
            if(anType== PatchMapping.class) {
                PatchMapping p = (PatchMapping)an;
                nextPath=getAnPath(p);
            }
        }
        if(StringUtils.isBlank(nextPath)) {
            return realUrl;
        }
        if(!nextPath.startsWith("/")) {
            nextPath="/"+nextPath;
        }
        if(realUrl.endsWith("/")) {
            realUrl= realUrl.substring(0,realUrl.length()-1);
        }
        realUrl+=nextPath;
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

    private static String getAnPath(RequestMapping rqsMapping) {
        String pathValue=null;
        String nextPaths [] = rqsMapping.value();
        if(nextPaths==null || nextPaths.length<=0) {
            nextPaths = rqsMapping.path();
        }
        if(nextPaths==null && nextPaths.length<=0) {
            return  null;
        }
        pathValue =nextPaths[0];
        return pathValue;
    }

    private static String getAnPath(GetMapping getMapping) {
        String pathValue=null;
        String nextPaths [] = getMapping.value();
        if(nextPaths==null || nextPaths.length<=0) {
            nextPaths = getMapping.path();
        }
        if(nextPaths==null && nextPaths.length<=0) {
            return  null;
        }
        pathValue =nextPaths[0];
        return pathValue;
    }

    private static String getAnPath(PostMapping postMapping) {
        String pathValue=null;
        String nextPaths [] = postMapping.value();
        if(nextPaths==null || nextPaths.length<=0) {
            nextPaths = postMapping.path();
        }
        if(nextPaths==null && nextPaths.length<=0) {
            return  null;
        }
        pathValue =nextPaths[0];
        return pathValue;
    }

    private static String getAnPath(PutMapping putMapping) {
        String pathValue=null;
        String nextPaths [] = putMapping.value();
        if(nextPaths==null || nextPaths.length<=0) {
            nextPaths = putMapping.path();
        }
        if(nextPaths==null && nextPaths.length<=0) {
            return  null;
        }
        pathValue =nextPaths[0];
        return pathValue;
    }

    private static String getAnPath(DeleteMapping delMapping) {
        String pathValue=null;
        String nextPaths [] = delMapping.value();
        if(nextPaths==null || nextPaths.length<=0) {
            nextPaths = delMapping.path();
        }
        if(nextPaths==null && nextPaths.length<=0) {
            return  null;
        }
        pathValue =nextPaths[0];
        return pathValue;
    }

    private static String getAnPath(PatchMapping patchMapping) {
        String pathValue=null;
        String nextPaths [] = patchMapping.value();
        if(nextPaths==null || nextPaths.length<=0) {
            nextPaths = patchMapping.path();
        }
        if(nextPaths==null && nextPaths.length<=0) {
            return  null;
        }
        pathValue =nextPaths[0];
        return pathValue;
    }

    private static String getPathValue(PathVariable pathVariable) {
        String pathValue=pathVariable.value();
        if(pathValue==null) {
            pathValue = pathVariable.name();
        }
        if(pathValue==null) {
            return "";
        }
        return pathValue;
    }

    public static <T> Map<String,Object> obj2Map(T obj) {
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            Class clazz = obj.getClass();
            while(clazz!=null && clazz!=Object.class)
            {
                Field[] fields = clazz.getDeclaredFields();
                for(Field field :fields)
                {
                    String fieldName = field.getName();
                    Object fieldValue = BeanUtils.getProperty(obj, fieldName);
                    map.put(fieldName,fieldValue);
                }
                clazz = clazz.getSuperclass();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    public List<String> getUrlPathNames(String url) {
        List<String> list =null;
        Pattern p = Pattern.compile(PATH_URL_PATTERN);
        Matcher mt=p.matcher(url);
        while(mt.find()) {
            String pathName =mt.group(1);
            if(list==null) {
                list= new ArrayList<String>();
            }
            list.add(pathName);
        }
        return list;
    }

    public String map2Str(Map<String,Object> map) {
        StringBuffer buffer= new StringBuffer("");
        if(map==null ||map.size()<=0) {
            return buffer.toString();
        }
        for(Map.Entry<String,Object> entry:map.entrySet()) {
            buffer.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
        }
        String rt =buffer.toString();
        return  rt;
    }
}