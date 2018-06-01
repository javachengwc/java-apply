package com.commonservice.sms.service.impl;

import com.commonservice.sms.config.SmsConfig;
import com.commonservice.sms.dao.mapper.SmsMapper;
import com.commonservice.sms.enums.SendResultEnum;
import com.commonservice.sms.enums.SmsTemplateEnum;
import com.commonservice.sms.enums.SmsTypeEnum;
import com.commonservice.sms.model.SmsAliSendResult;
import com.commonservice.sms.model.base.Resp;
import com.commonservice.sms.model.base.RespHeader;
import com.commonservice.sms.model.pojo.Sms;
import com.commonservice.sms.model.pojo.SmsExample;
import com.commonservice.sms.model.vo.SmsVo;
import com.commonservice.sms.service.SmsService;
import com.commonservice.sms.service.manager.SmsAliManager;
import com.util.date.CalendarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class SmsServiceImpl implements SmsService {

    private static Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    private static Random random = new Random();

    private static String randNumber = "0123456789";//随机产生的数字

    @Autowired
    private SmsMapper smsMapper;

    @Autowired
    private SmsConfig smsConfig;

    //今天发送成功的手机验证码个数
    public int todayCaptchaCount(String mobile) {
        SmsExample example = new SmsExample();
        SmsExample.Criteria criteria =example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        criteria.andSendResultEqualTo(SendResultEnum.SUCCESS.getValue());
        Date todayBegin = CalendarUtil.getFirstDateTimeOfDay(new Date());
        criteria.andSendTimeGreaterThanOrEqualTo(todayBegin);
        int count = smsMapper.countByExample(example);
        return count;
    }

    //查询最近的手机验证码
    public Sms queryLastedMobileCaptcha(String mobile) {
        SmsExample example = new SmsExample();
        SmsExample.Criteria criteria =example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        Date now = new Date();
        int effectDuration = smsConfig.getCaptchaTimeout();
        int beforeLong = -effectDuration*2;
        Date beforeTime =CalendarUtil.addMinutes(now,beforeLong);
        criteria.andSendTimeGreaterThan(beforeTime);
        example.setOrderByClause(" send_time desc limit 1 ");
        List<Sms> list = smsMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return  list.get(0);
        }
        return null;
    }

    //发送短信验证码
    public Resp<SmsVo> sendCaptcha(String mobile) {
        Resp<SmsVo> resp = new Resp<SmsVo>();
        //验证次数
        int todaySendCount= todayCaptchaCount(mobile);
        if(todaySendCount>=smsConfig.getMaxSendCount()) {
            String msg = "今天获取验证码次数已达上限";
            logger.info("SmsServiceImpl sendCaptcha fail msg={},todaySendCount={},mobile={}",
                    msg,todaySendCount,mobile);
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg(msg);
            return resp;
        }
        //产生验证码
        String captcha =genCaptcha();
        //添加短信记录
        Sms sms =addSms(mobile,captcha,captcha,SmsTypeEnum.CAPTCHA.getValue());
        //发送验证码
        SmsAliSendResult result =SmsAliManager.getInstance().sendCaptcha(mobile,captcha);
        //更新短信记录
        uptSms(sms,result);
        //组装返回结果
        boolean success =result.isSuccess();
        if(!success) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg(result.getMessage());
        }
        SmsVo smsVo= new SmsVo();
        smsVo.setSmsId(sms.getId());
        smsVo.setMobile(sms.getMobile());
        resp.setData(smsVo);
        return  resp;
    }

    public String genCaptcha() {
        String captcha="";
        for(int i=0;i<6;i++) {
            String rand =getRandomStr(randNumber,random.nextInt(randNumber.length()));
            captcha +=rand;
        }
        return captcha;
    }

    public String getRandomStr(String randStr,int num){
        return String.valueOf(randStr.charAt(num));
    }

    public Integer uptSms(Sms sms, SmsAliSendResult result) {
        Date now = new Date();
        Sms uptSms = new Sms();
        uptSms.setId(sms.getId());
        uptSms.setModifiedTime(now);
        int sendResult = result.isSuccess()?SendResultEnum.SUCCESS.getValue():SendResultEnum.FAIL.getValue();
        uptSms.setSendResult(sendResult);
        uptSms.setRtCode(result.getCode());
        uptSms.setRtMessage(result.getMessage());
        uptSms.setBizId(result.getBizId());
        Integer rt =smsMapper.updateByPrimaryKeySelective(uptSms);
        return rt;
    }

    public Sms addSms(String mobile,String content,String captcha,int smsType) {
        Date now = new Date();
        Sms sms = new Sms();
        sms.setCreateTime(now);
        sms.setModifiedTime(now);
        sms.setSendTime(now);
        int effectDuration = smsConfig.getCaptchaTimeout();
        Date expireTime =CalendarUtil.addMinutes(now,effectDuration);
        sms.setExpireTime(expireTime);
        sms.setType(smsType);
        sms.setMobile(mobile);
        sms.setContent(content);
        if(smsType==SmsTypeEnum.CAPTCHA.getValue()) {
            sms.setCaptcha(content);
        }
        sms.setFailCount(0);
        sms.setIsUse(0);
        sms.setSendResult(SendResultEnum.INIT.getValue());
        sms.setVerifyCount(0);
        smsMapper.insertSelective(sms);
        return sms;
    }

    //验证短信验证码
    public Boolean verifyCaptcha(String mobile,String captcha) {
        logger.info("SmsServiceImpl verifyCaptcha start ,mobile={},captcha={}",mobile,captcha);
        Sms lastedSms = queryLastedMobileCaptcha(mobile);
        if(lastedSms==null) {
            logger.info("SmsServiceImpl verifyCaptcha fail,not find sms record,mobile={},captcha={}",mobile,captcha);
            return false;
        }
        if(lastedSms.getIsUse()==1) {
            logger.info("SmsServiceImpl verifyCaptcha fail,captcha has used,mobile={},captcha={}",mobile,captcha);
            return false;
        }
        Date expireTime =lastedSms.getExpireTime();
        if(expireTime.getTime()<System.currentTimeMillis()) {
            logger.info("SmsServiceImpl verifyCaptcha fail,captcha has expired,mobile={},captcha={}",mobile,captcha);
            return false;
        }
        String orglCaptcha = lastedSms.getCaptcha();
        if(!orglCaptcha.equalsIgnoreCase(captcha)) {
            int verifyCount= lastedSms.getVerifyCount()==null?0:lastedSms.getVerifyCount();
            if(verifyCount>=smsConfig.getMaxVerifyCount()) {
                logger.info("SmsServiceImpl verifyCaptcha fail,captcha not equal and check count over max verify count," +
                        "mobile={},captcha={},verifyCount={}",mobile,captcha,verifyCount);
                return false;
            }
            uptSmsVerifyFail(lastedSms);
            logger.info("SmsServiceImpl verifyCaptcha fail,captcha not equal,mobile={},captcha={}",mobile,captcha);
            return false;
        }
        //更新验证码已使用
        uptSmsVerifySuccess(lastedSms);
        logger.info("SmsServiceImpl verifyCaptcha success,mobile={},captcha={}",mobile,captcha);
        return  true;
    }

    public Integer uptSmsVerifySuccess(Sms sms) {
        Sms uptSms = new Sms();
        uptSms.setId(sms.getId());
        uptSms.setModifiedTime(new Date());
        int verifyCount= sms.getVerifyCount()==null?0:sms.getVerifyCount();
        uptSms.setVerifyCount(verifyCount+1);
        uptSms.setIsUse(1);
        int rt =smsMapper.updateByPrimaryKeySelective(uptSms);
        return rt;
    }

    public Integer uptSmsVerifyFail(Sms sms) {
        Sms uptSms = new Sms();
        uptSms.setId(sms.getId());
        uptSms.setModifiedTime(new Date());
        int failCount = sms.getFailCount()==null?0:sms.getFailCount();
        int verifyCount= sms.getVerifyCount()==null?0:sms.getVerifyCount();
        uptSms.setFailCount(failCount+1);
        uptSms.setVerifyCount(verifyCount+1);
        int rt =smsMapper.updateByPrimaryKeySelective(uptSms);
        return rt;
    }

    //发送短信信息
    public Resp<SmsVo> sendMsg(String mobile,String template,Map<String, String> data) {
        Resp<SmsVo> resp = new Resp<SmsVo>();
        //添加记录
        String content=transContent(template,data);
        Sms sms =addSms(mobile,content,"", SmsTypeEnum.CONTENT.getValue());
        //发送短信
        SmsAliSendResult result =SmsAliManager.getInstance().send(mobile,template,data);
        //更新短信记录
        uptSms(sms,result);
        //组装返回结果
        boolean success =result.isSuccess();
        if(!success) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg(result.getMessage());
        }
        SmsVo smsVo= new SmsVo();
        smsVo.setSmsId(sms.getId());
        smsVo.setMobile(sms.getMobile());
        resp.setData(smsVo);
        return  resp;
    }

    public String transContent(String template,Map<String,String> data) {
        SmsTemplateEnum smsTemplateEnum=SmsTemplateEnum.getTemplateEnumByCode(template);
        if(smsTemplateEnum==null) {
            return "";
        }
        String content = smsTemplateEnum.getContent();
        for(Map.Entry<String,String> entry:data.entrySet()) {
            String key = entry.getKey();
            String value=entry.getValue();
            String replaceStr ="${"+key+"}";
            content=content.replace(replaceStr,value);
        }
        return content;
    }
}
