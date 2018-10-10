package com.shop.book.api.rest;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.api.model.vo.DictVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "字典接口")
@RequestMapping(value="/dict")
public interface DictResource {

    @ApiOperation(value = "查询所有字典", notes = "查询所有字典")
    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    public Resp<List<DictVo>> queryAll();

    @ApiOperation(value = "根据key获取字典", notes = "根据key获取字典")
    @RequestMapping(value = "/getByKey", method = RequestMethod.POST)
    public Resp<DictVo> getByKey(@RequestBody Req<String> req);

    @ApiOperation(value = "根据类型获取字典列表", notes = "根据类型获取字典列表")
    @RequestMapping(value = "/queryByType", method = RequestMethod.POST)
    public Resp<List<DictVo>> queryByType(@RequestBody Req<Integer> req);

}
