package com.solr7.index.web;

import com.solr7.index.scheduled.FullIndexFreshScheduled;
import com.solr7.index.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/index")
@Api(value = "索引相关接口")
@RestController
public class IndexCtrl {

    private static Logger logger= LoggerFactory.getLogger(IndexCtrl.class);

    @Autowired
    private IndexService indexService;

    @Autowired
    private FullIndexFreshScheduled fullIndexFreshScheduled;

    @ApiOperation(value = "增改索引", notes = "增改索引")
    @RequestMapping(value = "/addOrUptIndex", method = RequestMethod.GET)
    public Integer addOrUptIndex(@RequestParam(name="id") String id) {
        logger.info("IndexCtrl addOrUptIndex start,id ={}",id);
        int rt = indexService.addOrUptIndex(id);
        return rt;
    }

    @ApiOperation(value = "删除索引", notes = "删除索引")
    @RequestMapping(value = "/cancelIndex", method = RequestMethod.GET)
    public Integer cancelIndex(@RequestParam(name="id") String id) {
        logger.info("IndexCtrl cancelIndex start,id ={}",id);
        int rt = indexService.cancelIndex(id);
        return  rt;
    }

    @RequestMapping(value = "/freshIndex", method = RequestMethod.GET)
    public Integer freshIndex() {
        fullIndexFreshScheduled.freshIndex();
        return 1;
    }
}
