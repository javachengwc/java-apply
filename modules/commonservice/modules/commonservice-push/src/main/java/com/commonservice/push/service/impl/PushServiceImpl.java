package com.commonservice.push.service.impl;

import com.commonservice.push.config.Config;
import com.commonservice.push.enums.DeviceOsEnum;
import com.commonservice.push.enums.NoticeCastEnum;
import com.commonservice.push.enums.NoticeGoEnum;
import com.commonservice.push.enums.NoticeTypeEnum;
import com.commonservice.push.model.*;
import com.commonservice.push.model.base.Resp;
import com.commonservice.push.model.base.RespHeader;
import com.commonservice.push.model.vo.PushMessageVo;
import com.commonservice.push.service.PushService;
import com.commonservice.push.service.manager.PushManager;
import com.util.date.CalendarUtil;
import com.util.date.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PushServiceImpl implements PushService{

    private static Logger logger= LoggerFactory.getLogger(PushServiceImpl.class);

    private static String TARGET_ACTIVITY_KEY="target_activity";

    //推送消息
    public Resp<Void> push(PushMessageVo messageVo) {
        String deviceToken = messageVo.getDeviceToken();
        String message = messageVo.getMessage();
        logger.info("PushServiceImpl push start,deviceToken={},message={}",deviceToken,message);
        Resp<Void> resp= new Resp<Void>();
        String deviceOs =messageVo.getDeviceOs();
        UmengNotice umengNotice= null;
        if(!StringUtils.isBlank(deviceOs) && DeviceOsEnum.IPHONE.getValue().equalsIgnoreCase(deviceOs)) {
            umengNotice = genIosNotice(messageVo);
        }else {
            umengNotice= genAndroidNotice(messageVo);
        }
        boolean rt = PushManager.getInstance().push(umengNotice);
        if(!rt) {
            logger.info("PushServiceImpl push fail,messageVo={}",messageVo);
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("推送失败");
            return resp;
        }
        logger.info("PushServiceImpl push success,deviceToken={},message={}",deviceToken,message);
        return resp;
    }

    private UmengNotice genAndroidNotice(PushMessageVo messageVo) {
        String title = messageVo.getTitle();
        String message = messageVo.getMessage();
        UmengNotice umengNotice= new UmengNotice();
        umengNotice.setDeviceFlag(DeviceOsEnum.ANDROID.getFlag());
        umengNotice.setTimestamp(""+System.currentTimeMillis());
        umengNotice.setType(NoticeCastEnum.UNICAST.getValue());
        umengNotice.setProductionMode(Boolean.TRUE);
        umengNotice.setDeviceTokens(messageVo.getDeviceToken());
        umengNotice.setDescription(title);
        umengNotice.setMipush(Boolean.TRUE);
        umengNotice.setMiActivity(PushManager.getInstance().getMiActivity());
        AndroidPayload payload = new AndroidPayload();
        payload.setDisplayType(NoticeTypeEnum.NOTIFICATION.getValue());
        AndroidNoticeBody body = new AndroidNoticeBody();
        body.setTitle(title);
        body.setTicker(title);
        body.setText(message);
        body.setActivity(PushManager.getInstance().getMiActivity());
        body.setPlayLights(Boolean.FALSE.toString());
        body.setPlaySound(Boolean.FALSE.toString());
        body.setPlayVibrate(Boolean.FALSE.toString());
        body.setAfterOpen(NoticeGoEnum.GO_ACTIVITY.getValue());
        payload.setBody(body);
        Map<String,Object> extraMap = new HashMap<String,Object>();
        extraMap.put("reqId",messageVo.getReqId());
        extraMap.put("comeFrom",messageVo.getComeFrom());
        extraMap.put(TARGET_ACTIVITY_KEY,PushManager.getInstance().getTargetActivity());
        payload.setExtra(extraMap);
        umengNotice.setPayload(payload);
        return umengNotice;
    }

    private UmengNotice genIosNotice(PushMessageVo messageVo) {
        String title =messageVo.getTitle();
        String message = messageVo.getMessage();
        UmengNotice umengNotice= new UmengNotice();
        umengNotice.setDeviceFlag(DeviceOsEnum.IPHONE.getFlag());
        umengNotice.setTimestamp(""+System.currentTimeMillis());
        umengNotice.setType(NoticeCastEnum.UNICAST.getValue());
        boolean productMode=isProductEnv();
        umengNotice.setProductionMode(productMode);
        umengNotice.setDeviceTokens(messageVo.getDeviceToken());
        umengNotice.setDescription(title);
        NoticePolicy policy = new NoticePolicy();
        Date expireDate = CalendarUtil.addDates(new Date(),1);
        String expireDateStr =DateUtil.formatDate(expireDate,DateUtil.FMT_YMD_HMS);
        policy.setExpireTime(expireDateStr);
        umengNotice.setPolicy(policy);
        IosPayload payload = new IosPayload();
        IosAps aps= new IosAps();
        ApsAlert apsAlert = new ApsAlert();
        apsAlert.setBody(message);
        apsAlert.setTitle(title);
        aps.setAlert(apsAlert);
        payload.setAps(aps);
        payload.setReqId(messageVo.getReqId());
        payload.setComeFrom(messageVo.getComeFrom());
        umengNotice.setPayload(payload);
        return  umengNotice;
    }

    //是否生产环境
    public boolean isProductEnv() {
        boolean rt =true;
        String [] pls = Config.getActiveProfiles();
        if(pls!=null) {
            for(String per:pls) {
                if(per.equalsIgnoreCase("dev") || per.equalsIgnoreCase("test") ) {
                    rt=Boolean.FALSE;
                    break;
                }
            }
        }
        return rt;
    }
}
