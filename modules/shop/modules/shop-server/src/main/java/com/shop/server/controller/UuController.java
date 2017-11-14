package com.shop.server.controller;

import com.shop.server.model.Uu;
import com.shop.server.service.UuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "uu接口")
@RestController
@RequestMapping(value="/uu")
public class UuController {

    @Autowired
    private UuService uuService;

    @ApiOperation(value = "listUu", notes = "listUu")
    @RequestMapping(value="/listUu",method= RequestMethod.GET)
    public List<Uu> listUu(){
        List<Uu> list=new ArrayList<Uu>();

        Uu u1=new Uu();
        u1.setUsername("u1");
        list.add(u1);

        Uu u2=new Uu();
        u2.setUsername("u2");
        list.add(u2);

        Uu u3=new Uu();
        u3.setUsername("u3");
        list.add(u3);

        return list;
    }

    @ApiOperation(value="getUuById",notes="requires id")
    @RequestMapping(value="/{id}",method=RequestMethod.GET)
    public Uu getUuById(@PathVariable String id){
        Uu uu=new Uu();
        uu.setId(id);
        uu.setUsername("uu");
        return uu;
    }

    @ApiOperation(value="getDbUu",notes="getDbUu")
    @RequestMapping(value="/getDbUu",method=RequestMethod.GET)
    public List<Uu> getUuById(){
        Uu uu1=uuService.queryUu();
        Uu uu2=uuService.queryTbUu();
        List<Uu> list = new ArrayList<Uu>();
        if(uu1!=null) {
            list.add(uu1);
        }
        if(uu2!=null) {
            list.add(uu2);
        }
        return list;
    }
}
