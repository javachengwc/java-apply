package com.shop.web.controller;

import com.shop.web.model.pojo.SfProduct;
import com.shop.web.model.vo.SofaQueryVo;
import com.shop.web.service.SofaService;
import com.util.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/sofa")
public class SofaContrller {

    @Autowired
    private SofaService sofaService;

    @RequestMapping(value="/queryPage",method= RequestMethod.GET)
    public Page<SfProduct> queryPage(SofaQueryVo queryVo){
        queryVo.genPage();
        int pageNo = queryVo.getPage();
        int pageSize = queryVo.getRows();

        List<SfProduct> list = sofaService.queryPage(queryVo);
        int count = sofaService.count(queryVo);
        Page<SfProduct> page = new Page<SfProduct>(pageNo,pageSize,count,list);
        return page;
    }
}
