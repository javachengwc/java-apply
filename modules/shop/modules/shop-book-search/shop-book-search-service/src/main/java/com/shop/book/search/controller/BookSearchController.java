package com.shop.book.search.controller;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.base.util.TransUtil;
import com.shop.book.search.api.model.BookQueryVo;
import com.shop.book.search.api.model.BookSimpleVo;
import com.shop.book.search.api.rest.BookSearchResource;
import com.shop.book.search.model.BookSimple;
import com.shop.book.search.service.BookSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(value = "书籍检索接口")
@RequestMapping("/book/search")
@RestController
public class BookSearchController  implements BookSearchResource {

    private static Logger logger = LoggerFactory.getLogger(BookSearchController.class);

    @Autowired
    private BookSearchService bookSearchService;

    @ApiOperation(value = "书分页查询", notes = "书分页查询")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public Resp<Page<BookSimpleVo>> queryPage(@RequestBody @Validated Req<BookQueryVo> req, Errors errors) {
        logger.info("BookSearchController queryPage start,.................");
        Resp<Page<BookSimpleVo>> resp = new Resp<Page<BookSimpleVo>>();
        BookQueryVo queryVo = req.getData();
        if (queryVo == null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        queryVo.ready();
        Page<BookSimple> page = bookSearchService.queryPage(queryVo);
        List<BookSimple> list = page.getList();
        List<BookSimpleVo> rtList = TransUtil.transList(list,BookSimpleVo.class);

        Page<BookSimpleVo> rtPage = new Page<BookSimpleVo>();
        rtPage.setList(rtList);
        rtPage.setTotalCount(page.getTotalCount());

        resp.setData(rtPage);
        return resp;
    }

    @ApiOperation(value = "查询书数量", notes = "查询书数量")
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public Resp<Integer> count(@RequestBody @Validated Req<BookQueryVo> req, Errors errors) {
        logger.info("BookSearchController count start,.................");
        Resp<Integer> resp = new Resp<Integer>();
        BookQueryVo queryVo = req.getData();
        if (queryVo == null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        queryVo.ready();
        int count = bookSearchService.count(queryVo);
        resp.setData(count);
        return resp;
    }
}
