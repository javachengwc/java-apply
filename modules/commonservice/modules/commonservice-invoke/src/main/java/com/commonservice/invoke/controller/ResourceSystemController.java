package com.commonservice.invoke.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.commonservice.invoke.model.entity.ResourceSystem;
import com.commonservice.invoke.model.vo.ResourceSystemVo;
import com.commonservice.invoke.service.ResourceSystemService;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.TransUtil;
import com.util.page.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "接口系统")
@RestController
@RequestMapping("/resource/system")
@Slf4j
public class ResourceSystemController {

    @Autowired
    private ResourceSystemService resourceSystemService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询接口系统",notes = "分页查询接口系统")
    public Resp<com.util.page.Page<ResourceSystemVo>> page(@RequestBody Req<PageQuery<ResourceSystemVo>> req){
        PageQuery<ResourceSystemVo> query= req.getData();
        log.info("ResourceSystemController page start,query={}", JsonUtil.obj2Json(query));
        query.genStart();

        ResourceSystem condition = TransUtil.transEntity(query.getEntity(),ResourceSystem.class);
        Wrapper<ResourceSystem> wrapper = new QueryWrapper<ResourceSystem>(condition);
        IPage<ResourceSystem> pageParam = new Page<ResourceSystem>(query.getStart(),query.getPageSize());

        IPage<ResourceSystem> pageData = resourceSystemService.page(pageParam,wrapper);

        com.util.page.Page<ResourceSystemVo> rtPage = new com.util.page.Page<ResourceSystemVo>();
        rtPage.setResult(TransUtil.transListWithJson(pageData.getRecords(),ResourceSystemVo.class));
        rtPage.setTotalCount(new Long(pageData.getTotal()).intValue());

        return Resp.data(rtPage);
    }

    @GetMapping("/getById")
    @ApiOperation("查询接口系统")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "接口系统id", required = true, dataType = "Long", paramType = "query")
    })
    public Resp<ResourceSystem> getById(@RequestParam(value = "id") Long id){
        log.info("ResourceSystemController getById start,id={}", id);
        ResourceSystem data=resourceSystemService.getById(id);
        return Resp.data(data);
    }

    @GetMapping("/list")
    @ApiOperation("查询接口系统列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "optionAll", value = "是否包含全部", required = false, dataType = "Integer", paramType = "query")
    })
    public Resp<List<ResourceSystemVo>> list(@RequestParam(value = "optionAll",required = false) Integer optionAll){
        log.info("ResourceSystemController list start......");
        List<ResourceSystem> list=resourceSystemService.listBySort();
        if(optionAll!=null && optionAll==1) {
            //包含全部
            ResourceSystem optionAllSystem= new ResourceSystem();
            optionAllSystem.setName("全部");
            optionAllSystem.setSort(0);
            list.add(0,optionAllSystem);
        }
        List<ResourceSystemVo> rtList = TransUtil.transList(list,ResourceSystemVo.class);
        return Resp.data(rtList);
    }
}
