package com.boot.controller;

import com.boot.util.I18nUtil;
import com.boot.util.LocaleUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/i18n")
public class I18nController {

    private static Logger logger= LoggerFactory.getLogger(I18nController.class);

    @Autowired
    private I18nUtil i18nUtil;

    @ApiOperation(value="getMessage")
    @RequestMapping(value="/getMessage",method= RequestMethod.GET)
    public String getMessage(String key) {
        logger.info("getMessage key={}",key);
        String language = LocaleUtil.getLanguage();
        String value =i18nUtil.getMessage(key);
        logger.info("getMessage key={},value={},language={}",key,value,language);
        return value;
    }
}
