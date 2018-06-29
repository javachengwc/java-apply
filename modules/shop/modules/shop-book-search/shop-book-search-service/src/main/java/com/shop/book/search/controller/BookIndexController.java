package com.shop.book.search.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.book.search.api.rest.BookIndexResource;
import com.shop.book.search.scheduled.BookIndexScheduled;
import com.shop.book.search.service.BookIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "书籍索引接口")
@RequestMapping("/book/index")
@RestController
public class BookIndexController implements BookIndexResource {

    private static Logger logger= LoggerFactory.getLogger(BookIndexController.class);

    @Autowired
    private BookIndexService bookIndexService;

    @Autowired
    private BookIndexScheduled bookIndexScheduled;

    @ApiOperation(value = "添加或更新书索引", notes = "添加或更新书索引")
    @RequestMapping(value = "/addOrUptIndex", method = RequestMethod.POST)
    public Resp<Void> addOrUptIndex(@RequestBody Req<Long> req) {
        Long bookId = req.getData();
        logger.info("BookIndexController addOrUptIndex start,bookId ={}",bookId);
        Resp<Void> resp = new Resp<Void>();
        if(bookId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        int rt = bookIndexService.addOrUptIndex(bookId);
        int resultCode = rt>=0?RespHeader.SUCCESS:RespHeader.FAIL;
        resp.getHeader().setCode(resultCode);
        return resp;
    }

    @ApiOperation(value = "删除书索引", notes = "删除书索引")
    @RequestMapping(value = "/cancelIndex", method = RequestMethod.POST)
    public Resp<Void> cancelIndex(@RequestBody Req<Long> req) {
        Long bookId = req.getData();
        logger.info("BookIndexController cancelIndex start,bookId ={}",bookId);
        Resp<Void> resp = new Resp<Void>();
        if(bookId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        int rt = bookIndexService.cancelIndex(bookId);
        int resultCode = rt>=0?RespHeader.SUCCESS:RespHeader.FAIL;
        resp.getHeader().setCode(resultCode);
        return resp;
    }

    @RequestMapping(value = "/freshIndex", method = RequestMethod.GET)
    public Integer freshIndex() {
        bookIndexScheduled.freshIndex();
        return 0;
    }
}
