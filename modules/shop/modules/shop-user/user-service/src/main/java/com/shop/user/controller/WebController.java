package com.shop.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/web")
public class WebController {

    @ResponseBody
    @RequestMapping(value="/getWebInfo",method= RequestMethod.GET)
    public Map<String,Object> getWebInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("contextPath",request.getContextPath());
        map.put("requestURI",request.getRequestURI());
        map.put("requestURL",request.getRequestURL().toString());
        map.put("servletPath",request.getServletPath());

        String url = session.getServletContext().getRealPath("/");
        map.put("webappRootPath", url);
        return map;
    }
}
