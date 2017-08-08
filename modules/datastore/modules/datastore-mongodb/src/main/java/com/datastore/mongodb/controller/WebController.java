package com.datastore.mongodb.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @ResponseBody
    @RequestMapping("getWebInfo")
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
}
