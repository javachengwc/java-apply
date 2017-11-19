package com.shop.web.controller;

import com.shop.web.model.pojo.SfProduct;
import com.shop.web.model.vo.SofaQueryVo;
import com.shop.web.service.SofaService;
import com.util.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private SofaService sofaService;

    //首页
    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView=new ModelAndView("template");

        SofaQueryVo queryVo = new SofaQueryVo();
        queryVo.genPage();

        int pageNo = queryVo.getPage();
        int pageSize = queryVo.getRows();
        List<SfProduct> list = sofaService.queryPage(queryVo);
        int count = sofaService.count(queryVo);
        Page<SfProduct> page = new Page<SfProduct>(pageNo,pageSize,count,list);

        modelAndView.addObject("sofaList", list);
        modelAndView.addObject("page", page);
        modelAndView.addObject("baseUrl", getBaseUrl());
        modelAndView.addObject("contentUrl","home/home.ftl");
        modelAndView.addObject("navName","home");

        return modelAndView;
    }

    @RequestMapping("/home")
    public ModelAndView home(){
        ModelAndView modelAndView=new ModelAndView("index/index");

        SofaQueryVo queryVo = new SofaQueryVo();
        queryVo.genPage();

        int pageNo = queryVo.getPage();
        int pageSize = queryVo.getRows();
        List<SfProduct> list = sofaService.queryPage(queryVo);
        int count = sofaService.count(queryVo);
        Page<SfProduct> page = new Page<SfProduct>(pageNo,pageSize,count,list);

        modelAndView.addObject("sofaList", list);
        modelAndView.addObject("page", page);
        modelAndView.addObject("baseUrl", getBaseUrl());
        modelAndView.addObject("navName","home");

        return modelAndView;
    }
}
