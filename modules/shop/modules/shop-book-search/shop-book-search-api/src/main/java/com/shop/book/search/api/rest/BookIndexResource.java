package com.shop.book.search.api.rest;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/book/index")
@Api(value = "书索引接口")
@RestController
public interface BookIndexResource {

    @ApiOperation(value = "添加或更新书索引", notes = "添加或更新书索引")
    @RequestMapping(value = "/addOrUptIndex", method = RequestMethod.POST)
    public Resp<Void> addOrUptIndex(@RequestBody Req<Long> req);

    @ApiOperation(value = "删除书索引", notes = "删除书索引")
    @RequestMapping(value = "/cancelIndex", method = RequestMethod.POST)
    public Resp<Void> cancelIndex(@RequestBody Req<Long> req);

    @RequestMapping(value = "/freshIndex", method = RequestMethod.GET)
    public Integer freshIndex();
}
