package com.shop.book.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.util.TransUtil;
import com.shop.book.api.model.vo.QaVo;
import com.shop.book.api.rest.QaResource;
import com.shop.book.model.pojo.Qa;
import com.shop.book.service.QaService;
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

@Api(value = "qa接口")
@RestController
@RequestMapping("/qa")
public class QaController implements QaResource {

    private static Logger logger = LoggerFactory.getLogger(QaController.class);

    @Autowired
    private QaService qaService;

    @ApiOperation(value = "问答列表", notes = "问答列表")
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public Resp<List<QaVo>> queryList(@RequestBody @Validated Req<Void> req, Errors errors) {
        Resp<List<QaVo>> resp = new Resp<List<QaVo>>();
        List<Qa> list =qaService.queryAll();
        List<QaVo> rtList = TransUtil.transList(list,QaVo.class);
        resp.setData(rtList);
        return  resp;
    }

}
