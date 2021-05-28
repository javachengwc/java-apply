package com.manage.rbac.controller;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.SystemDTO;
import com.manage.rbac.model.vo.SystemSimpleVo;
import com.model.base.Resp;
import com.springdubbo.controller.BaseController;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/system")
@Api(value="系统接口",description="系统接口")
@Slf4j
public class SystemController extends BaseController {

    @Autowired
    private DubboFactory dubboFactory;

    @GetMapping("/listSystem")
    @ApiOperation("查询系统列表")
    public Resp<List<SystemSimpleVo>> listSystem(){
        Resp<List<SystemDTO>> rt =dubboFactory.getSystemProvider().listAbleSystem();
        List<SystemSimpleVo> list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),SystemSimpleVo.class);
        return Resp.data(list);

    }
}
