package com.cloud.zkapp1b.controller;

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
@RequestMapping("/web")
public class WebController {

    private static Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private DiscoveryClient client;

    @ResponseBody
    @RequestMapping("/getWebInfo")
    public Map<String,Object> getWebInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        logger.info("WebController getWebInfo invoked.................");

        Map<String,Object> map  = new HashMap<String,Object>();
        map.put("contextPath",request.getContextPath());
        map.put("requestURI",request.getRequestURI());
        map.put("requestURL",request.getRequestURL().toString());
        map.put("servletPath",request.getServletPath());
        String url = session.getServletContext().getRealPath("/");
        map.put("webappRootPath",url);

        return map;
    }

    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    public Integer add(@RequestParam Integer a, @RequestParam Integer b) {
        ServiceInstance instance = client.getLocalServiceInstance();
        Integer r = a + b;
        logger.info("WebController zkapp1b add, host:" + instance.getHost() + ", serviceId:" + instance.getServiceId() + ", result:" + r);
        return r;
    }

}
