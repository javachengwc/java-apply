package com.es.consumer.controller;

import com.es.consumer.model.vo.WebInfo;
import com.model.base.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/web")
@Api(value="web接口",description="web接口")
@Slf4j
public class WebController {

    @ResponseBody
    @GetMapping("/getWebInfo")
    @ApiOperation("查询web信息")
    public Resp<WebInfo> getWebInfo(HttpServletRequest request, HttpSession session) {
        WebInfo webInfo= new WebInfo();
        webInfo.setContextPath(request.getContextPath());
        webInfo.setRequestURI(request.getRequestURI());
        webInfo.setRequestURL(request.getRequestURI());
        webInfo.setServletPath(request.getRequestURI());
        String url = session.getServletContext().getRealPath("/");
        webInfo.setWebappRootPath(url);
        Resp<WebInfo> resp = new Resp<WebInfo>(webInfo);
        return resp;
    }

}
