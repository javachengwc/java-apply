package com.component.rest.springmvc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RestTemplateUtil {

    private static Logger logger= LoggerFactory.getLogger(RestTemplateUtil.class);

    public static Map<String,HttpMethod> httpMethodMap= new HashMap<String,HttpMethod>();

//    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        httpMethodMap.put("GET",HttpMethod.GET);
        httpMethodMap.put("HEAD",HttpMethod.HEAD);
        httpMethodMap.put("POST",HttpMethod.POST);
        httpMethodMap.put("PUT",HttpMethod.PUT);
        httpMethodMap.put("PATCH",HttpMethod.PATCH);
        httpMethodMap.put("DELETE",HttpMethod.DELETE);
        httpMethodMap.put("OPTIONS",HttpMethod.OPTIONS);
        httpMethodMap.put("TRACE",HttpMethod.TRACE);
    }

    //restTemplate自身支持的GET方法
    public static <T> T getForObject(RestTemplate restTemplate,String url,
                                     Class<T> responseType, Object... urlVariables) throws RestClientException {

        //String result = restTemplate.getForObject("http://example.com/aa/{p1}/bb/{p2}", String.class,"p11", "p22");
        return restTemplate.getForObject(url,responseType,urlVariables);
    }
    public static <T> T getForObject(RestTemplate restTemplate,String url,
                                     Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {

        //Map<String, String> vars = Collections.singletonMap("p1", "p11");
        //String result = restTemplate.getForObject("http://example.com/aa/{p1}", String.class, vars);
        return restTemplate.getForObject(url,responseType,urlVariables);
    }

    public static <T> T getForObject(RestTemplate restTemplate,URI url,
                                     Class<T> responseType) throws RestClientException {

        //String result=restTemplate.getForObject("http://example.com/aa?id=1&name=ab", String.class );
        return restTemplate.getForObject(url,responseType);
    }

    //restTemplate自身支持的POST方法
    public static <T> T postForObject(RestTemplate restTemplate,String url, Object request,
                                      Class<T> responseType, Object... uriVariables) throws RestClientException {
        return restTemplate.postForObject(url,request,responseType,uriVariables);
    }

    public static <T> T postForObject(RestTemplate restTemplate,String url, Object request,
                                      Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
//
//        HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//
//        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
//        postParameters.add("p1", "p11");
//        postParameters.add("p2", "p22");
//        HttpEntity<MultiValueMap<String, String>> requestEntity  = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
//        ResultVo resultVo = restTemplate.postForObject("http://example.com/bb",  requestEntity, ResultVo.class,null);
//
        return restTemplate.postForObject(url,request,responseType,uriVariables);
    }

    public static <T> T postForObject(RestTemplate restTemplate,URI url, Object request,
                                      Class<T> responseType) throws RestClientException {

//        Entity entity = new Entity();
//        entity.setId(id);
//        entity.setName(name);
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//        String responseResult = restTemplate.postForObject(url, entity, String.class);
        return restTemplate.postForObject(url,request,responseType);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static <T> T request(RestTemplate restTemplate, String url, String  httpMethod,
                                Map<String,Object> pathParams,Map<String, Object> queryParams,
                                Object body,MultiValueMap<String,String> headers,
                                Class<T> returnClass,boolean isParamType,Type returnType){

        String invokeUrl =url;
        if(pathParams!=null && pathParams.size()>0) {
            for(String key:pathParams.keySet()) {
                String value =pathParams.get(key).toString();
                invokeUrl=invokeUrl.replace(key,value);
            }
        }
        if(queryParams!=null && queryParams.size()>0) {
            StringBuffer buffer =new StringBuffer();
            for(String key:queryParams.keySet()) {
                String value =queryParams.get(key).toString();
                buffer.append(key).append("=").append(value).append("&");
            }
            String queryStr ="?"+buffer.toString();
            if(queryStr.endsWith("&")) {
                queryStr=queryStr.substring(0,queryStr.length()-1);
            }
            invokeUrl=invokeUrl+queryStr;
        }
        logger.info("RestTemplateUtil request invokeUrl={},isParamType={},returnClass={}",invokeUrl,isParamType,returnClass);
        HttpEntity<Object> requestEntity=null;
        if(body!=null) {
            requestEntity  = new HttpEntity<Object>(body, headers);
        }
        HttpMethod httpMd =transHttpMethod(httpMethod);

        T resps=null;
        if(!isParamType) {
            if("GET".equalsIgnoreCase(httpMethod)) {
                resps =restTemplate.getForEntity(invokeUrl,returnClass).getBody();
            } else {
                //postForEntity
                resps = restTemplate.exchange(url, httpMd, requestEntity, returnClass).getBody();
            }
        } else {
            if("GET".equalsIgnoreCase(httpMethod)) {
                resps =restTemplate.getForEntity(invokeUrl,returnClass).getBody();
                //泛型处理有问题
            } else {
                resps = (T) restTemplate.exchange(invokeUrl,httpMd, requestEntity,
                        new ParameterizedTypeReference<Object>() {
                            public Type getType() {
                                return returnType;
                            }
                        }
                ).getBody();
            }
        }
        logger.info("--------------------RestTemplateUtil request over,invokeUrl={},resps={}",invokeUrl,resps);
        return resps;

    }

    public static HttpMethod transHttpMethod(String httpMethod) {
        return httpMethodMap.get(httpMethod);
    }
//
//    public static String obj2Json(Object obj) {
//        String json = null;
//        try {
//            json = objectMapper.writeValueAsString(obj);
//        } catch (Exception  e) {
//            logger.info("RestTemplateUtil obj2Json error,",e);
//        }
//        return json;
//    }
//
//    public static void main(String args [])  throws  Exception {
//        String url ="http://localhost:8288/order/getOrderInfo2";
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//
//        Req<Long> reqst = new Req<Long>();
//        reqst.setData(12004L);
//
//        HttpEntity<String> formEntity = new HttpEntity<String>(null, headers);
//
//        Rep<SimpleOrderVo> resps = new Rep<SimpleOrderVo>();
//        final Class returnClazz =resps.getClass();
//        //resps= (Rep<SimpleOrderVo>)request(restTemplate,url,"POST",null,reqst,headers,null,returnClazz);
//
//        Method method= OrderRest.class.getMethod("query",Long.class);
//        Type returnType =method.getGenericReturnType();
//        //resps=(Rep<SimpleOrderVo>)restTemplate.getForObject(url+"?orderId=1",returnClazz);
//        Type tp = ((ParameterizedType)returnType).getActualTypeArguments()[0];
//
//        System.out.println("------------------------------");
//        resps= (Rep<SimpleOrderVo>)restTemplate.exchange(url+"?orderId=1",
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<Object>() {
//                    @Override
//                    public Type getType() {
//                        return returnType;
//                    }
//                }
//        ).getBody();
//        if(resps==null) {
//            System.out.println("resps is null");
//        } else {
//            System.out.println(resps);
//            System.out.println("---------------"+resps.getData().getOrderNo());
//        }
//    }
}
