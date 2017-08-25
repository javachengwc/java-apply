package com.cloud.appa.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("web")
public class WebController {

    private static Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private DiscoveryClient client;

    @ResponseBody
    @RequestMapping("getWebInfo")
    public Map<String,Object> getWebInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {

        logger.info("WebController getWebInfo invoked.................");

        Map<String,Object> map  = new HashMap<String,Object>();

        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //		request.getContextPath();/mll
        //		request.getRequestURI(); /mll/web/getWebInfo.do
        //		request.getRequestURL(); http://localhost:8081/mll/web/getWebInfo.do
        //		request.getServletPath(); /web/getWebInfo.do

        map.put("contextPath",request.getContextPath());
        map.put("requestURI",request.getRequestURI());
        map.put("requestURL",request.getRequestURL().toString());
        map.put("servletPath",request.getServletPath());

        //webapp的path F:\workproject\java-application\modules\webapp\modules\app-z7z8\src\main\webapp
        String url = session.getServletContext().getRealPath("/");
        map.put("webappRootPath",url);

        return map;
    }

    @RequestMapping(value = "redirect", method = RequestMethod.GET)
    public String redirect(@RequestParam(value = "url") String url, HttpServletRequest request)
    {
        logger.info("WebController redirect invoked.................");

        if(!StringUtils.isBlank(url) && (url.startsWith("http") ||  url.startsWith("www")))
        {
            return "redirect:" + url;
        } else {
            return "";
        }
    }

    @RequestMapping(value = "add" ,method = RequestMethod.GET)
    public Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b) {
        ServiceInstance instance = client.getLocalServiceInstance();
        Integer r = a + b;
        logger.info("WebController add, host:" + instance.getHost() + ", serviceId:" + instance.getServiceId() + ", result:" + r);
        return r;
    }

}
