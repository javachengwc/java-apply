package com.shop.book.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.util.TransUtil;
import com.shop.book.api.model.vo.DictVo;
import com.shop.book.api.rest.DictResource;
import com.shop.book.model.pojo.Dict;
import com.shop.book.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "字典接口")
@RestController
@RequestMapping("/dict")
public class DictController implements DictResource {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "查询所有字典", notes = "查询所有字典")
    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    public Resp<List<DictVo>> queryAll() {
        List<Dict> list = dictService.queryAll();
        List<DictVo> rtList = TransUtil.transList(list,DictVo.class);
        Resp<List<DictVo>> resp = Resp.success(rtList);
        return  resp;
    }

    @ApiOperation(value = "根据key获取字典", notes = "根据key获取字典")
    @RequestMapping(value = "/getByKey", method = RequestMethod.POST)
    public Resp<DictVo> getByKey(@RequestBody Req<String> req) {
        String dictKey = req.getData();
        if(StringUtils.isBlank(dictKey)) {
            return Resp.error("字典key不能为空");
        }
        Dict dict = dictService.getByKey(dictKey);
        DictVo dictVo=TransUtil.transEntity(dict,DictVo.class);
        Resp<DictVo> resp = Resp.success(dictVo);
        return  resp;
    }

    @ApiOperation(value = "根据类型获取字典列表", notes = "根据类型获取字典列表")
    @RequestMapping(value = "/queryByType", method = RequestMethod.POST)
    public Resp<List<DictVo>> queryByType(@RequestBody Req<Integer> req) {
        Integer dictType = req.getData();
        if(dictType==null) {
            return Resp.error("字典类型不能为空");
        }
        List<Dict> list = dictService.queryByType(dictType,null);
        List<DictVo> rtList=TransUtil.transList(list,DictVo.class);
        Resp<List<DictVo>> resp = Resp.success(rtList);
        return  resp;
    }
}
