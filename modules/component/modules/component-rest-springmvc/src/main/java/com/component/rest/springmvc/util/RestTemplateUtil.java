package com.component.rest.springmvc.util;

import javax.ws.rs.core.Cookie;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestTemplateUtil {

    private static Logger logger= LoggerFactory.getLogger(RestTemplateUtil.class);

    public static Map<String,HttpMethod> httpMethodMap= new HashMap<String,HttpMethod>();

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

    public static <T> T post(RestTemplate restTemplate, String url, Map<String, Object> params,Class<T> returnClass) {
        return request(restTemplate, url, "POST", params,null,null,null,returnClass);
    }

    public static  <T> T  get(RestTemplate restTemplate, String url, Map<String, Object> params,Class<T> returnClass) {
        return request(restTemplate, url, "GET",params,null,null,null,returnClass);
    }

    public static  <T> T  delete(RestTemplate restTemplate, String url, Map<String, Object> params,Class<T> returnClass) {
        return request(restTemplate, url, "DELETE", params,null,null,null,returnClass);
    }

    public static  <T> T  put(RestTemplate restTemplate, String url, Map<String, Object> params,Class<T> returnClass) {
        return request(restTemplate, url, "PUT", params,null,null,null,returnClass);
    }

    public static <T> T request(RestTemplate restTemplate, String url, String  httpMethod,
                                           Map<String, Object> params,Object body,
                                           MultiValueMap<String,String> headers,
                                           List<Cookie> cookies ,Class<T> returnClass) {

        HttpEntity<String> requestEntity=null;
        if(body!=null) {
            String bodyJsonStr =new Gson().toJson(body);
            requestEntity = new HttpEntity<String>(bodyJsonStr, headers);
        }
        ResponseEntity<T> rt = restTemplate.exchange(url, transHttpMethod(httpMethod), requestEntity, returnClass);
        if(rt==null) {
            return null;
        }
        HttpStatus httpStatus =rt.getStatusCode();
        logger.info("RestTemplateUtil request return httpStatus={},url={}",httpStatus,url);
        T t= rt.getBody();
        System.out.println("--------------------t:"+t);
        return t;
    }

    public static HttpMethod transHttpMethod(String httpMethod) {
        return httpMethodMap.get(httpMethod);
    }


    public static void main(String args []) {
        String url ="http://ccc.com/aa";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        Req<Long> reqst = new Req<Long>();
        reqst.setData(12004L);

        //HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);

        Rep<SimpleOrderVo> resps = new Rep<SimpleOrderVo>();
        Class returnClazz =resps.getClass();
        resps= (Rep<SimpleOrderVo>)request(restTemplate,url,"POST",null,reqst,headers,null,returnClazz);
        if(resps==null) {
            System.out.println("resps is null");
        } else {
            System.out.println(resps);
        }
    }
}
