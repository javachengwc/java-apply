package com.commonservice.invoke.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/web")
public class WebController {

    @ResponseBody
    @RequestMapping("/getWebInfo")
    public Object getWebInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Map map = new HashMap();
        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //		request.getContextPath();/mll
        //		request.getRequestURI(); /mll/web/getWebInfo.do
        //		request.getRequestURL(); http://localhost:8081/mll/web/getWebInfo.do
        //		request.getServletPath(); /web/getWebInfo.do

        map.put("contextPath",request.getContextPath());
        map.put("requestURI",request.getRequestURI());
        map.put("requestURL",request.getRequestURL().toString());
        map.put("servletPath",request.getServletPath());
        map.put("localAddr",request.getLocalAddr());
        return map;
    }
}
