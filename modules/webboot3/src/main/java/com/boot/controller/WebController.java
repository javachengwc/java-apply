package com.boot.controller;

import com.boot.config.ApplicationConfig;
import com.boot.vo.RespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@Tag(name = "web控制类", description = "WebController")
public class WebController {

    @Autowired
    private ApplicationConfig applicationConfig;

    @GetMapping(value = "/")
    @ResponseBody
    public RespVO index() {
        return RespVO.success();
    }

    @GetMapping(value = "/checkEnv")
    @ResponseBody
    @Operation(method="checkEnv",description = "服务的基本信息")
    public RespVO<Map<String, Object>> checkEnv() {
        Map<String, Object> map = new HashMap<>();
        map.put("profile", applicationConfig.getProfile());
        map.put("appName", applicationConfig.getAppName());
        map.put("port", applicationConfig.getPort());
        return RespVO.data(map);
    }

    @GetMapping("/health")
    @ResponseBody
    @Operation(method="health",description = "监控检查方法")
    public RespVO health() {
        return RespVO.success("检测成功！");
    }

    @RequestMapping(value = "redirect", method = RequestMethod.GET)
    public String redirect(@RequestParam(value = "url") String url, HttpServletRequest request)
    {
        if(!StringUtils.isBlank(url) && (url.startsWith("http") ||  url.startsWith("www")))
        {
            return "redirect:" + url;
        } else {
            return "";
        }
    }
}
