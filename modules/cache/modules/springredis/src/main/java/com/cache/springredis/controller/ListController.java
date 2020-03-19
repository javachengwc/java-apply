package com.cache.springredis.controller;

import com.cache.springredis.model.vo.DataVo;
import com.cache.springredis.service.ListService;
import com.model.base.Req;
import com.model.base.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list")
@Api(value="list接口",description="list接口")
public class ListController {

    @Autowired
    private ListService listService;

    @GetMapping("/listData")
    @ApiOperation("查询数据列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "count", value = "数量", required = false, dataType = "Integer", paramType = "query")
    })
    public Resp<List<DataVo>> listData( @RequestParam(value = "count", required = false) Integer count) {
        if(count==null) {
            count=100;
        }
        List<DataVo> list =listService.listData(count);
        return Resp.data(list);
    }

    @PostMapping("/addData")
    @ApiOperation("添加数据")
    public Resp<Void> addData(@RequestBody Req<DataVo> req){
        DataVo dataVo = req.getData();
        listService.addData(dataVo);
        return Resp.success();
    }

    @GetMapping("/countList")
    @ApiOperation("查询数据数量")
    public Resp<Long> countList(){
        Long count=listService.countData();
        return Resp.data(count);
    }

}
