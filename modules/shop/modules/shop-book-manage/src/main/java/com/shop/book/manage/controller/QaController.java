package com.shop.book.manage.controller;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.manage.model.vo.QaQueryVo;
import com.shop.book.manage.model.vo.QaVo;
import com.shop.book.manage.service.QaService;
import com.util.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Api(value = "问答接口", description = "问答接口")
@RestController
@RequestMapping("/qa")
public class QaController {

    @Autowired
    private QaService qaService;

    @ApiOperation(value = "分页查询问答", notes = "分页查询问答")
    @PostMapping("/queryPage")
    public Resp<Page<QaVo>> queryPage(@Validated @RequestBody Req<QaQueryVo> req, Errors errors) {
        QaQueryVo queryVo = req.getData();
        String endDateStr = queryVo.getEndDate();
        if(StringUtils.isNotBlank(endDateStr)){
            Date endDate = DateUtil.getDate(endDateStr,DateUtil.FMT_YMD);
            Date nextDate = DateUtil.addDates(endDate,1);
            queryVo.setEndDate(DateUtil.formatDate(nextDate,DateUtil.FMT_YMD));
        }
        queryVo.genPage();

        Page<QaVo> page= qaService.queryPage(queryVo);

        Resp<Page<QaVo>> resp = Resp.success(page,"成功");
        return  resp;
    }
}
