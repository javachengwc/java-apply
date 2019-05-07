package com.storefront.manage.controller;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.storefront.manage.model.vo.BrandQueryVo;
import com.storefront.manage.model.vo.BrandVo;
import com.storefront.manage.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(value = "品牌接口", description = "品牌接口")
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "分页查询品牌", notes = "分页查询品牌")
    @PostMapping("/queryPage")
    public Resp<Page<BrandVo>> queryPage(@Validated @RequestBody Req<BrandQueryVo> req, Errors errors) {
        BrandQueryVo queryVo = req.getData();
        queryVo.genPage();

        Page<BrandVo> page= brandService.queryPage(queryVo);

        Resp<Page<BrandVo>> resp = Resp.success(page,"成功");
        return  resp;
    }
}
