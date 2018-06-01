package com.commonservice.sms.controller;

import com.commonservice.sms.model.base.Req;
import com.commonservice.sms.model.base.Resp;
import com.commonservice.sms.model.base.RespHeader;
import com.commonservice.sms.model.pojo.Sms;
import com.commonservice.sms.model.vo.SmsVo;
import com.commonservice.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "短信接口")
@RestController
@RequestMapping("/sms")
public class SmsController {

    private static Logger logger= LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private SmsService smsService;

    @ApiOperation(value = "发送手机验证码", notes = "发送手机验证码")
    @RequestMapping(value = "/sendCaptcha", method = RequestMethod.POST)
    public Resp<SmsVo> sendCaptcha(@RequestBody Req<String> req) {
        String mobile =req.getData();
        logger.info("SmsController sendCaptcha start,mobile={}",mobile);
        Resp<SmsVo> resp = new Resp<SmsVo>();
        if(StringUtils.isBlank(mobile)) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数错误");
            return resp;
        }
        resp= smsService.sendCaptcha(mobile);
        return resp;
    }

    @ApiOperation(value = "验证手机验证码", notes = "验证手机验证码")
    @RequestMapping(value = "/verifyCaptcha", method = RequestMethod.POST)
    public Resp<Void> verifyCaptcha(@RequestBody Req<SmsVo> req) {
        SmsVo smsVo =req.getData();
        String mobile = smsVo==null?null:smsVo.getMobile();
        String captcha = smsVo==null?null:smsVo.getContent();
        logger.info("SmsController chechCaptcha start,mobile={},captcha={}",mobile,captcha);
        Resp<Void> resp = new Resp<Void>();
        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(captcha)) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数错误");
            return resp;
        }
        boolean rt = smsService.verifyCaptcha(mobile,captcha);
        if(!rt) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("验证失败");
        }
        return resp;
    }
}
