package com.commonservice.invoke.controller.system;

import com.commonservice.invoke.model.vo.MenuVo;
import com.commonservice.invoke.service.system.MenuService;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.http.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Api(description = "菜单接口")
@RestController
@RequestMapping("/menu")
@Slf4j
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/getMenu")
    @ApiOperation("查询菜单")
    public Resp<List<MenuVo>> getMenu(){
        log.info("MenuController getMenu start........");
        Resp<List<MenuVo>> resp = new Resp<List<MenuVo>>(Collections.EMPTY_LIST);
        String dataPath = "/static/json/menu.json";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String selfUrl = request.getRequestURL().toString();
        String selfPath = request.getServletPath();
        String url = selfUrl.replace(selfPath,dataPath);
        try {
            String data=HttpUtil.sendGet(url, "");
            List<MenuVo> list = JsonUtil.json2Obj(data,List.class,MenuVo.class);
            resp.setData(list);
        } catch (Exception e) {
            log.error("MenuController getMenu error,",e);
        }
        return resp;
    }
}
