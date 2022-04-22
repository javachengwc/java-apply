package com.commonservice.invoke.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/run", method = RequestMethod.GET)
    public String run() {
        return "run";
    }

    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    public String resource() {
        return "resource";
    }
}
