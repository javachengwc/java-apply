package com.micro.basic.controller;

import com.micro.basic.entity.vo.NationVo;
import com.micro.basic.model.pojo.Nation;
import com.micro.basic.service.NationService;
import com.model.base.Req;
import com.model.base.Resp;
import com.shop.base.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nation")
@Api(value="民族接口",description="民族接口")
public class NationController {

    @Autowired
    private NationService nationService;

    @PostMapping("/list")
    @ApiOperation("民族列表")
    public Resp<List<NationVo>> list(@RequestBody Req<Void> req) {
        List<Nation> list = nationService.queryAll();
        List<NationVo> rtList = TransUtil.transList(list,NationVo.class);
        return Resp.data(rtList);
    }
}
