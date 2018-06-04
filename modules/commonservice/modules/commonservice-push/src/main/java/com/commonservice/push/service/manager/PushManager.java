package com.commonservice.push.service.manager;

import com.commonservice.push.enums.DeviceOsEnum;
import com.commonservice.push.model.UmengNotice;
import com.commonservice.push.model.UmengResp;
import com.commonservice.push.util.JsonUtil;
import com.commonservice.push.util.SpringContextUtil;
import com.util.encrypt.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class PushManager {

    private static Logger logger = LoggerFactory.getLogger(PushManager.class);

    private static String SEND_PATH="/send";

    private static String UPUSH_ANDROID_APPKEY="upush.android.appkey";

    private static String UPUSH_ANDROID_MASTER_SECRET="upush.android.master.secret";

    private static String UPUSH_IOS_APPKEY="upush.ios.appkey";

    private static String UPUSH_IOS_MASTER_SECRET="upush.ios.master.secret";

    private static String UPUSH_URL="upush.url";

    private static String MI_ACTIVITY="mi.activity";

    private static String TARGET_ACTIVITY="target.activity";

    private static PushManager instance= new PushManager();

    public static PushManager getInstance() {
        return instance;
    }

    private String upushAndroidAppkey;

    private String upushAndroidMasterSecret;

    private String upushIosAppkey;

    private String upushIosMasterSecret;

    //http://msg.umeng.com/api
    private String upushUrl;

    private String miActivity;

    private String targetActivity;

    public RestTemplate restTemplate;

    private PushManager() {
        init();
    }

    public void init() {
        logger.info("PushManager init start.............................");
        Environment environment = SpringContextUtil.getApplicationContext().getEnvironment();
        upushAndroidAppkey=environment.getProperty(UPUSH_ANDROID_APPKEY);
        upushAndroidMasterSecret=environment.getProperty(UPUSH_ANDROID_MASTER_SECRET);
        upushIosAppkey=environment.getProperty(UPUSH_IOS_APPKEY);
        upushIosMasterSecret=environment.getProperty(UPUSH_IOS_MASTER_SECRET);
        upushUrl=environment.getProperty(UPUSH_URL);
        miActivity=environment.getProperty(MI_ACTIVITY);
        targetActivity=environment.getProperty(TARGET_ACTIVITY);
        logger.info("PushManager init read config param ," +
                "upushAndroidAppkey={}," +
                "upushAndroidMasterSecret={}," +
                "upushIosAppkey={}," +
                "upushIosMasterSecret={}," +
                "upushUrl={}",
                upushAndroidAppkey,upushAndroidMasterSecret,upushIosAppkey,upushIosMasterSecret,upushUrl);
        restTemplate=SpringContextUtil.getBean(RestTemplate.class);
        logger.info("PushManager init end.............................");
    }

    public String getMiActivity() {
        return miActivity;
    }

    public String getTargetActivity() {
        return targetActivity;
    }

    //推送消息
    public boolean push(UmengNotice notice) {
        logger.info("PushManager push start ,notice={}", notice.toString());
        String appkey = upushAndroidAppkey;
        String masterSecret = upushAndroidMasterSecret;
        Integer flag =notice.getDeviceFlag();
        if(flag!=null && flag== DeviceOsEnum.IPHONE.getFlag().intValue()) {
            appkey=upushIosAppkey;
            masterSecret=upushIosMasterSecret;
        }
        notice.setAppkey(appkey);
        String domainUrl = upushUrl + SEND_PATH;
        String postBody = JsonUtil.obj2Json(notice);
        String signStr = genSign(HttpMethod.POST, domainUrl, postBody,masterSecret);
        String url = domainUrl + "?sign=" + signStr;
        logger.info("PushManager push url={},postBody={}",url,postBody);
        try {
            HttpHeaders headers = genJsonHeader();
            HttpEntity<String> requestEntity = new HttpEntity<String>(postBody, headers);
            UmengResp<Object> resp = (UmengResp<Object>) restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity,new ParameterizedTypeReference<UmengResp<Object>>() {}
            ).getBody();
            logger.info("PushManager push invoke remote end, url={},resp={}", url, resp);
        } catch (Exception e) {
            logger.error("PushManager push error,", e);
            return false;
        }
        return true;
    }

    //产生签名
    //MD5($http_method$url$post-body$app_master_secret)
    public String genSign(HttpMethod httpMethod, String domainUrl, String postBody,String masterSecret) {
        String originStr = httpMethod.toString() + domainUrl + postBody + masterSecret;
        try {
            String signStr = MD5.encodeMd5Hex(originStr);
            logger.info("PushManager genSign originStr={},signStr={}", originStr, signStr);
            return signStr;
        }catch(Exception e) {
            logger.error("PushManager genSign error,originStr={}", originStr, e);
            return null;
        }
    }

    public HttpHeaders genJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return headers;
    }

}
