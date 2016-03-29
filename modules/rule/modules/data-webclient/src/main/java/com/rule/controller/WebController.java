package com.rule.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("web")
public class WebController {

    @ResponseBody
    @RequestMapping("getWebInfo")
    public JSONObject getWebInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        JSONObject json = new JSONObject();

        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //		request.getContextPath();/mll
        //		request.getRequestURI(); /mll/web/getWebInfo.do
        //		request.getRequestURL(); http://localhost:8081/mll/web/getWebInfo.do
        //		request.getServletPath(); /web/getWebInfo.do

        json.put("contextPath",request.getContextPath());
        json.put("requestURI",request.getRequestURI());
        json.put("requestURL",request.getRequestURL().toString());
        json.put("servletPath",request.getServletPath());

        //webapp的path F:\workproject\java-application\modules\webapp\modules\app-z7z8\src\main\webapp
        String url = session.getServletContext().getRealPath("/");
        json.put("webappRootPath",url);

        return json;
    }
}
