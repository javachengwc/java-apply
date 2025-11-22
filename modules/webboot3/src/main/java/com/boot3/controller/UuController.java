package com.boot3.controller;

import com.boot3.model.Uu;
import com.boot3.service.UuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/uu")
@Tag(name = "uu控制类", description = "UuController")
public class UuController {

    @Autowired
    private UuService uuService;

    @ResponseBody
    @RequestMapping(value="/queryUu", method = RequestMethod.GET)
    @Operation(method="queryUU",description = "查询uu")
    public Uu queryUu(){
        return uuService.queryUu();
    }
}
