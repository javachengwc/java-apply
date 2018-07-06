package com.shop.book.api.rest;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.api.model.vo.QaVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "问答接口")
@RequestMapping(value="/qa")
public interface QaResource {

    @ApiOperation(value = "问答列表", notes = "问答列表")
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public Resp<List<QaVo>> queryList(@RequestBody @Validated Req<Void> req, Errors errors);
}
