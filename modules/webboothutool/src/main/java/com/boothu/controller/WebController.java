package com.boothu.controller;

import com.model.base.Resp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("web")
public class WebController {

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    @ResponseBody
    public Resp<String> hello(Model model, @RequestParam(value="name") String name) {
        return Resp.data("hello "+name);
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

}
