package com.component.rest.springmvc.util;

import javax.ws.rs.core.Cookie;

import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestTemplateUtil {

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
