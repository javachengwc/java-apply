package com.storefront.manage.controller;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.storefront.manage.model.vo.StoreQueryVo;
import com.storefront.manage.model.vo.StoreVo;
import com.storefront.manage.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "门店接口", description = "门店接口")
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "分页查询门店", notes = "分页查询门店")
    @PostMapping("/queryPage")
    public Resp<Page<StoreVo>> queryPage(@Validated @RequestBody Req<StoreQueryVo> req, Errors errors) {
        StoreQueryVo queryVo = req.getData();
        queryVo.genPage();

        Page<StoreVo> page= storeService.queryPage(queryVo);

        Resp<Page<StoreVo>> resp = Resp.success(page,"成功");
        return  resp;
    }

}
