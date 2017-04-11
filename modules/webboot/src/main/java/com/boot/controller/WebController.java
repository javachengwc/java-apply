package com.boot.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

        //webapp的path F:\workproject\java-application\modules\webapp\modules\webboot\src\main\webapp
        String url = session.getServletContext().getRealPath("/");
        json.put("webappRootPath",url);

        return json;
    }

    @RequestMapping(value = "redirect", method = RequestMethod.GET)
    public String redirectStat(@RequestParam(value = "url") String url, HttpServletRequest request)
    {
        if(!StringUtils.isBlank(url) && (url.startsWith("http") ||  url.startsWith("www")))
        {
            return "redirect:" + url;
        } else {
            return "";
        }
    }

    @RequestMapping("/hello")
    public String hello(Model model,@RequestParam(value="name") String name) {
        model.addAttribute("name", name);
        return "hello";
    }

}
