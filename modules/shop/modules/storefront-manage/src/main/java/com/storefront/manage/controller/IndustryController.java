package com.storefront.manage.controller;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.storefront.manage.model.vo.IndustryQueryVo;
import com.storefront.manage.model.vo.IndustryVo;
import com.storefront.manage.service.IndustryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "行业接口", description = "行业接口")
@RestController
@RequestMapping("/industry")
public class IndustryController {

    @Autowired
    private IndustryService qaService;

    @ApiOperation(value = "分页查询行业", notes = "分页查询行业")
    @PostMapping("/queryPage")
    public Resp<Page<IndustryVo>> queryPage(@Validated @RequestBody Req<IndustryQueryVo> req, Errors errors) {
        IndustryQueryVo queryVo = req.getData();
        queryVo.genPage();

        Page<IndustryVo> page= qaService.queryPage(queryVo);

        Resp<Page<IndustryVo>> resp = Resp.success(page,"成功");
        return  resp;
    }
}
