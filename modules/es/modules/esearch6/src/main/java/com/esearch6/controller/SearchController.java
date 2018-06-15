package com.esearch6.controller;

import com.esearch6.model.SearchVo;
import com.esearch6.service.SearchService;
import com.util.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/search")
@Api(value = "检索接口")
@RestController
public class SearchController {

    private static Logger logger= LoggerFactory.getLogger(SearchController.class);

    @Autowired
    public SearchService searchService;

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public Page<Map<String,Object>> queryPage(@RequestBody SearchVo searchVo) {
        logger.info("SearchCtrl queryPage start,.................");
        Page<Map<String,Object>> page =searchService.queryPage(searchVo);
        return  page;
    }

    @ApiOperation(value = "查询总数", notes = "查询总数")
    @RequestMapping(value = "/count" , method = RequestMethod.POST)
    public Integer count(@RequestBody SearchVo searchVo) {
        logger.info("SearchCtrl count start,.................");
        int count =searchService.count(searchVo);
        return  count;
    }
}
