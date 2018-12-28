package com.shop.book.manage.controller;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.manage.model.vo.DictQueryVo;
import com.shop.book.manage.model.vo.DictVo;
import com.shop.book.manage.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(value = "字典接口", description = "字典接口")
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "分页查询字典", notes = "分页查询字典")
    @PostMapping("/queryPage")
    public Resp<Page<DictVo>> queryPage(@Validated @RequestBody Req<DictQueryVo> req, Errors errors) {
        DictQueryVo queryVo = req.getData();
        queryVo.genPage();

        Page<DictVo> page= dictService.queryPage(queryVo);

        Resp<Page<DictVo>> resp = Resp.success(page,"成功");
        return  resp;
    }
}
