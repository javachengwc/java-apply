package com.boot.controller;

import com.boot.model.Uu;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * restful风格
 * rest是一组架构约束条件和原则。满足这些约束条件和原则的应用就是restful。
 */
@RestController
@RequestMapping(value="/rest")
public class RestfulController {

    @ApiOperation(value="listUu")
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
}
