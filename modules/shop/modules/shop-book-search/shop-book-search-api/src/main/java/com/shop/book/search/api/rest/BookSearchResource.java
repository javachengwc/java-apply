package com.shop.book.search.api.rest;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.search.api.model.BookQueryVo;
import com.shop.book.search.api.model.BookSimpleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "书搜索接口")
@RequestMapping("/book/search")
@RestController
public interface BookSearchResource {

    @ApiOperation(value = "书分页查询", notes = "书分页查询")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public  Resp<Page<BookSimpleVo>>  queryPage(@RequestBody @Validated Req<BookQueryVo> req, Errors errors);

    @ApiOperation(value = "查询书数量", notes = "查询书数量")
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public Resp<Integer> count(@RequestBody @Validated Req<BookQueryVo> req, Errors errors);
}
