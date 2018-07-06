package com.shop.book.api.rest;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.api.model.vo.AdvertVo;
import com.shop.book.api.model.vo.QaVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "广告接口")
@RequestMapping(value="/advert")
public interface AdvertResource {

    @ApiOperation(value = "根据位置查询广告", notes = "根据位置查询广告")
    @RequestMapping(value = "/queryByPosition", method = RequestMethod.POST)
    public Resp<List<AdvertVo>> queryByPosition(@RequestBody @Validated Req<String> req, Errors errors);

    @ApiOperation(value = "重载广告", notes = "重载广告")
    @RequestMapping(value = "/reloadAdvert", method = RequestMethod.POST)
    public Resp<Void> reloadAdvert(@RequestBody Req<Void> req);
}
