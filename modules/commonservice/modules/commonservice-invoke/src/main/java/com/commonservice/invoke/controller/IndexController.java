package com.commonservice.invoke.controller;

import com.commonservice.invoke.model.vo.InvokeVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(Model model) {
        InvokeVo invokeVo = new InvokeVo();
        invokeVo.setResourceLink("aa");
        model.addAttribute("invokeVo",invokeVo);
        return "test";
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
