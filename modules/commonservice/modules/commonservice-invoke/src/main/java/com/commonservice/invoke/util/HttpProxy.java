package com.commonservice.invoke.util;

import com.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

public class HttpProxy {

    private static Logger logger = LoggerFactory.getLogger(HttpProxy.class);

    public static final String GET_METHOD = "GET";

    public static final String POST_METHOD = "POST";

    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final int CONNECTION_TIMEOUT_MS = 200*1000;//200秒

    public static final int SO_TIMEOUT_MS = 200*1000;

    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(50);
        manager.setDefaultMaxPerRoute(20);
        CloseableHttpClient client = HttpClientBuilder.create().setConnectionManager(manager).build();
        httpClient=client;
    }

    public static HttpResponse invoke(String url, String httpMethod,
                                      Map<String, Object> params, Map<String, String> headers,Map<String,String> cookies,
                                      String contentType) throws Exception {
        return invoke(url,httpMethod,params,headers,cookies,contentType,UTF8);
    }

    public static HttpResponse invoke(String url, String httpMethod,
                                       Map<String, Object> params, Map<String, String> headers,Map<String,String> cookies,
                                       String contentType,Charset charset) throws Exception {
        HttpRequestBase req = null;
        if(GET_METHOD.equalsIgnoreCase(httpMethod)) {
            req = genHttpGet(url, params);
        }
        if(POST_METHOD.equalsIgnoreCase(httpMethod)) {
            req = genHttpPost(url, params, contentType);
        }
        if(req==null) {
            throw new RuntimeException("不支持["+httpMethod+"]方法调用");
        }
        appendHeader(req,headers,contentType);
        req.setConfig(genRequestConfig());

        HttpResponse httpResponse = new HttpResponse();
        CloseableHttpResponse response= null;
        CloseableHttpClient exeClient = httpClient;
        if(cookies!=null && cookies.size()>0) {
            CookieStore cookieStore = new BasicCookieStore();
            exeClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            logger.info("HttpProxy invoke has cookies,cookies="+JsonUtil.obj2Json(cookies)+" ,url=" +url);
            appendCookie(req,cookieStore,cookies);
        }
        try{
            response = exeClient.execute(req);
            int rtCode= (response==null)?0:response.getStatusLine().getStatusCode();
            httpResponse.setCode(rtCode);
            if( HttpStatus.SC_OK!=rtCode) {
                httpResponse.setSuccess(false);
                logger.info("HttpProxy invoke rt code="+rtCode+",url=" +url+
                        ",thread name="+Thread.currentThread().getName());
                req.abort();
            } else {
                httpResponse.setSuccess(true);

                Header [] allHeaders = response.getAllHeaders();
                Map<String,String> headerMap= new HashMap<String,String>();
                if(allHeaders!=null) {
                    for (Header per : allHeaders) {
                        headerMap.put(per.getName(), per.getValue());
                    }
                }
                //设置header头信息
                httpResponse.setHeaders(headerMap);

                Header [] contentTypeHeaders= response.getHeaders("Content-Type");
                String respContentType = ( contentTypeHeaders==null || contentTypeHeaders.length<=0) ?"":contentTypeHeaders[0].getValue();
                httpResponse.setContentType(respContentType);

                Header [] contentDispositions= response.getHeaders("Content-Disposition");
                String respContentDispositions = ( contentDispositions==null || contentDispositions.length<=0) ?"":contentDispositions[0].getValue();
                //返回结果是否为文件
                boolean respIsFile = StringUtils.isNotBlank(respContentDispositions) && respContentDispositions.startsWith("attachment");
                httpResponse.setBeFile(respIsFile);

                HttpEntity entity = response == null ? null : response.getEntity();
                if (entity != null) {
                    if(respIsFile) {
                        //返回结果为文件
                        String filename = "";
                        int index = respContentDispositions.indexOf("filename=");
                        if(index>=0) {
                            filename =respContentDispositions.substring(index+"filename=".length());
                        } else {
                            Header [] filenameHeaders= response.getHeaders("filename");
                            filename= ( filenameHeaders==null || filenameHeaders.length<=0) ?"":filenameHeaders[0].getValue();
                        }
                        httpResponse.setFilename(filename);
                        byte[] data = EntityUtils.toByteArray(entity);
                        httpResponse.setBody(data);
                        logger.info("HttpProxy invoke rt is file, filename="+filename);
                    } else {
                        String returnStr = EntityUtils.toString(entity, charset);
                        httpResponse.setBody(returnStr);
                        EntityUtils.consume(entity);
                        if (StringUtils.isNotBlank(returnStr)) {
                            try {
                                Object obj = JsonUtil.json2Obj(returnStr, Object.class);
                                if (obj != null) {
                                    httpResponse.setBody(obj);
                                    httpResponse.setJson(true);
                                }
                            } catch (Exception ee) {
                                logger.warn("HttpProxy invoke json2Obj returnStr fail ,", ee);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("HttpProxy invoke error,httpMethod=" +httpMethod+",url="+url+
                    ",thread name="+Thread.currentThread().getName(),e);
            req.abort();
            throw new RuntimeException("服务调用异常");
        } finally {
            if(response!=null) {
                response.close();
            }
        }
        return httpResponse;
    }

    public static HttpGet genHttpGet(String url, Map<String, Object> params) throws URISyntaxException {
        HttpGet get = new HttpGet(genGetUrl(url, params));
        return get;
    }

    private static String genGetUrl(String url, Map<String, Object> params) {

        StringBuffer uriStr = new StringBuffer(url);
        if (params != null) {
            List<NameValuePair> ps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                boolean valueExist = params.get(key) !=null;
                if(valueExist) {
                    Object valueObject = params.get(key);
                    String value = valueObject.toString();
                    if(valueObject instanceof Map) {
                        logger.info("HttpProxy genGetUrl param {} is Object ",key);
                        value = JsonUtil.obj2Json(valueObject);
                    }
                    ps.add(new BasicNameValuePair(key, value));
                }
            }
            uriStr.append("?");
            uriStr.append(URLEncodedUtils.format(ps, UTF8));
        }
        String rtUrl = uriStr.toString();
        logger.info("HttpProxy genGetUrl rtUrl={}",rtUrl);
        return rtUrl;
    }

    public static HttpPost genHttpPost(String url, Map<String,Object> params, String contentType) throws Exception {
        HttpPost post = new HttpPost(url);
        if(params==null || params.size()<=0) {
            return post;
        }
        if(StringUtils.isNotBlank(contentType) && contentType.startsWith(CONTENT_TYPE_JSON )) {
            String bodyJsonStr = JsonUtil.obj2Json(params);
            post.setEntity(new StringEntity(bodyJsonStr, UTF8));
            return post;
        }
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            boolean valueExist = params.get(key) !=null;
            if(valueExist) {
                list.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
        }
        post.setEntity(new UrlEncodedFormEntity(list,UTF8));
        return post;
    }

    public static RequestConfig genRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
        return requestConfig;
    }

    public static void appendHeader(HttpRequestBase req,Map<String,String> headers,String contentType) {
        if(headers!=null) {
            for(Map.Entry<String,String> per: headers.entrySet()) {
                req.setHeader(per.getKey(),per.getValue());
            }
        }
        if(StringUtils.isNotBlank(contentType) && (headers==null || !headers.containsKey(HTTP.CONTENT_TYPE))) {
            req.setHeader(HTTP.CONTENT_TYPE,contentType);
        }
    }

    public static void appendCookie(HttpRequestBase req,CookieStore cookieStore,Map<String,String> cookies) {
        if(cookies==null || cookies.size()<=0) {
            return ;
        }
        URI uri = req.getURI();
        String host = uri.getHost();
        for(Map.Entry<String,String> per: cookies.entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(per.getKey(),per.getValue());
            cookie.setDomain(host);
            cookie.setPath("/");
            cookie.setVersion(0);
            cookieStore.addCookie(cookie);
        }
    }
}
