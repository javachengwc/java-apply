package com.commonservice.invoke.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.commonservice.invoke.model.entity.ResourceCategory;
import com.commonservice.invoke.model.param.CategoryTreeParam;
import com.commonservice.invoke.model.param.ResourceCategoryQuery;
import com.commonservice.invoke.model.vo.ResourceCategoryVo;
import com.commonservice.invoke.model.vo.TreeSelectVo;
import com.commonservice.invoke.service.ResourceCategoryService;
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

import java.util.ArrayList;
import java.util.List;

@Api(description = "接口目录")
@RestController
@RequestMapping("/resource/category")
@Slf4j
public class ResourceCategoryController {

    @Autowired
    private ResourceCategoryService resourceCategoryService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询接口目录",notes = "分页查询接口目录")
    public Resp<com.util.page.Page<ResourceCategoryVo>> page(@RequestBody Req<PageQuery<ResourceCategoryVo>> req){
        PageQuery<ResourceCategoryVo> query= req.getData();
        log.info("ResourceCategoryController page start,query={}", JsonUtil.obj2Json(query));
        query.genStart();

        ResourceCategory condition = TransUtil.transEntity(query.getEntity(),ResourceCategory.class);
        Wrapper<ResourceCategory> wrapper = new QueryWrapper<ResourceCategory>(condition);
        IPage<ResourceCategory> pageParam = new Page<ResourceCategory>(query.getStart(),query.getPageSize());

        IPage<ResourceCategory> pageData = resourceCategoryService.page(pageParam,wrapper);

        com.util.page.Page<ResourceCategoryVo> rtPage = new com.util.page.Page<ResourceCategoryVo>();
        rtPage.setResult(TransUtil.transListWithJson(pageData.getRecords(),ResourceCategoryVo.class));
        rtPage.setTotalCount(new Long(pageData.getTotal()).intValue());

        return Resp.data(rtPage);
    }

    @GetMapping("/getById")
    @ApiOperation("查询接口目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "接口目录id", required = true, dataType = "Long", paramType = "query")
    })
    public Resp<ResourceCategory> getById(@RequestParam(value = "id") Long id){
        log.info("ResourceCategoryController getById start,id={}", id);
        ResourceCategory data=resourceCategoryService.getById(id);
        return Resp.data(data);
    }

    @GetMapping("/tree")
    @ApiOperation("查询类别树")
    public Resp<List<TreeSelectVo>> tree(CategoryTreeParam cateTreeParam){
        String name = cateTreeParam == null? "": cateTreeParam.getName();
        Long parentId = cateTreeParam== null ?null: cateTreeParam.getParentId();
        Long sysId = cateTreeParam==null ? null: cateTreeParam.getSysId();
        log.info("ResourceCategoryController tree start,name={},parentId={},sysId={}",name,parentId,sysId);
        ResourceCategoryQuery query = TransUtil.transEntity(cateTreeParam,ResourceCategoryQuery.class);
        List<ResourceCategory> list = resourceCategoryService.queryList(query);
        List<ResourceCategory> cateList = resourceCategoryService.genTree(list);
        List<TreeSelectVo> rtList = TransUtil.transList(cateList, new TransUtil.ITrans<TreeSelectVo, ResourceCategory>() {
            @Override
            public TreeSelectVo doTrans(ResourceCategory data) {
                TreeSelectVo treeSelectVo = new TreeSelectVo();
                treeSelectVo.setId(data.getId());
                treeSelectVo.setLabel(data.getName());
                List<ResourceCategory> list = data.getChildren();
                if(list!=null && list.size() > 0) {
                    List<TreeSelectVo> childList = new ArrayList<TreeSelectVo>();
                    for(ResourceCategory per: list) {
                        TreeSelectVo child = doTrans(per);
                        childList.add(child);
                    }
                    treeSelectVo.setChildren(childList);
                }
                return treeSelectVo;
            }
        });
        return Resp.data(rtList);
    }
}
