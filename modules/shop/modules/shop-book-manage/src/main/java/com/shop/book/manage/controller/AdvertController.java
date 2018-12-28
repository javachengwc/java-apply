package com.shop.book.manage.controller;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.manage.model.vo.AdvertQueryVo;
import com.shop.book.manage.model.vo.AdvertVo;
import com.shop.book.manage.service.AdvertService;
import com.util.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Api(value = "广告接口", description = "广告接口")
@RestController
@RequestMapping("/advert")
public class AdvertController {

    @Autowired
    private AdvertService advertService;

    @ApiOperation(value = "分页查询广告", notes = "分页查询广告")
    @PostMapping("/queryPage")
    public Resp<Page<AdvertVo>> queryPage(@Validated @RequestBody Req<AdvertQueryVo> req, Errors errors) {
        AdvertQueryVo queryVo = req.getData();
        String endDateStr = queryVo.getEndDate();
        if(StringUtils.isNotBlank(endDateStr)){
            Date endDate = DateUtil.getDate(endDateStr,DateUtil.FMT_YMD);
            Date nextDate = DateUtil.addDates(endDate,1);
            queryVo.setEndDate(DateUtil.formatDate(nextDate,DateUtil.FMT_YMD));
        }
        queryVo.genPage();

        Page<AdvertVo> page= advertService.queryPage(queryVo);

        Resp<Page<AdvertVo>> resp = Resp.success(page,"成功");
        return  resp;
    }

}
