package com.boot.controller;

import com.boot.model.Uu;
import com.boot.service.UuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/uu")
public class UuController {

    @Autowired
    private UuService uuService;

    @ResponseBody
    @RequestMapping("/queryUu")
    public Uu queryUu(){
        return uuService.queryUu();
    }
}
