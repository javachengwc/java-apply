package com.shop.web.controller;

import com.shop.web.model.pojo.SfBrand;
import com.shop.web.model.pojo.SfMater;
import com.shop.web.model.pojo.SfStyle;
import com.shop.web.model.pojo.SfType;
import com.shop.web.service.SfBrandService;
import com.shop.web.service.SfMaterService;
import com.shop.web.service.SfStyleService;
import com.shop.web.service.SfTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/sofa-attr")
public class SofaAttributeController {

    @Autowired
    private SfBrandService sfBrandService;

    @Autowired
    private SfMaterService sfMaterService;

    @Autowired
    private SfTypeService sfTypeService;

    @Autowired
    private SfStyleService sfStyleService;


    @RequestMapping(value="/brandList",method= RequestMethod.GET)
    public List<SfBrand> brandList(){
        return sfBrandService.queryAbleList();
    }

    @RequestMapping(value="/materList",method= RequestMethod.GET)
    public List<SfMater> materList(){
        return sfMaterService.queryAll();
    }

    @RequestMapping(value="/typeList",method= RequestMethod.GET)
    public List<SfType> typeList(){
        return sfTypeService.queryAll();
    }

    @RequestMapping(value="/styleList",method= RequestMethod.GET)
    public List<SfStyle> styleList(){
        return sfStyleService.queryAll();
    }
}
