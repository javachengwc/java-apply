package com.micro.basic.controller;

import com.micro.basic.entity.vo.CityVo;
import com.micro.basic.entity.vo.ProvinceVo;
import com.micro.basic.model.pojo.City;
import com.micro.basic.model.pojo.Province;
import com.micro.basic.service.CityService;
import com.micro.basic.service.ProvinceService;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/area")
@Slf4j
@Api(value="地域接口",description="地域接口")
public class AreaController {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @RequestMapping(value="/queryProvCityList",method = RequestMethod.POST)
    @ApiOperation("省城市列表")
    public Resp<List<CityVo>> queryProvCityList(@RequestBody Req<String> req) {
        String provinceCode=req.getData();
        if(StringUtils.isBlank(provinceCode)) {
            return Resp.data(Collections.emptyList());
        }
        List<City> list = cityService.queryCityList(provinceCode);
        List<CityVo> rtList = TransUtil.transList(list,CityVo.class);
        Resp<List<CityVo>> resp = new Resp<List<CityVo>>(rtList);
        return resp;
    }

    @RequestMapping(value="/provinceList",method = RequestMethod.POST)
    @ApiOperation("省份列表")
    public Resp<List<ProvinceVo>> provinceList(@RequestBody Req<Void> req) {
        List<Province> list = provinceService.queryAll();
        List<ProvinceVo> rtList = TransUtil.transList(list,ProvinceVo.class);
        return Resp.data(rtList);
    }
}
