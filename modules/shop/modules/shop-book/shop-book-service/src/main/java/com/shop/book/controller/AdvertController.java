package com.shop.book.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.util.TransUtil;
import com.shop.book.api.model.vo.AdvertVo;
import com.shop.book.api.rest.AdvertResource;
import com.shop.book.dao.mapper.AdvertMapper;
import com.shop.book.model.pojo.Advert;
import com.shop.book.service.manager.AdvertManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "广告接口")
@RequestMapping(value="/advert")
@RestController
public class AdvertController implements AdvertResource {

    @ApiOperation(value = "根据位置查询广告", notes = "根据位置查询广告")
    @RequestMapping(value = "/queryByPosition", method = RequestMethod.POST)
    public Resp<List<AdvertVo>> queryByPosition(@RequestBody @Validated Req<String> req, Errors errors) {
        Resp<List<AdvertVo>> resp =new Resp<List<AdvertVo>>();
        String positionCode = req.getData();
        if(StringUtils.isBlank(positionCode)) {
            return resp;
        }
        List<Advert> list = AdvertManager.getInstance().getAdvertByPosition(positionCode);
        List<AdvertVo> rtList = TransUtil.transList(list,AdvertVo.class);
        resp.setData(rtList);
        return resp;
    }

    @ApiOperation(value = "重载广告", notes = "重载广告")
    @RequestMapping(value = "/reloadAdvert", method = RequestMethod.POST)
    public Resp<Void> reloadAdvert(@RequestBody Req<Void> req) {
        AdvertManager.getInstance().init();
        Resp<Void> resp = new Resp<>();
        return resp;
    }
}
