package com.otd.boot.finance.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.model.base.PageVo;
import com.otd.boot.component.util.TransUtil;
import com.otd.boot.finance.model.entity.FinanceAccount;
import com.otd.boot.finance.model.vo.FinanceAccountSearch;
import com.otd.boot.finance.model.vo.FinanceAccountVo;
import com.otd.boot.finance.service.FinanceAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financeAccount")
@Api(value = "财务记账接口", description = "财务记账接口")
public class FinanceAccountController {

    @Autowired
    private FinanceAccountService financeAccountService;

    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    @ApiOperation("分页查询财务记账")
    public PageVo<FinanceAccountVo> queryPage(FinanceAccountSearch search) {
        PageHelper.startPage(search.getPageNum(), search.getPageSize());
        PageInfo<FinanceAccount> pageInfo = new PageInfo<>(financeAccountService.queryFinanceAccount(search));
        PageVo<FinanceAccountVo> page = new PageVo<>();
        page.setList(TransUtil.transList(pageInfo.getList(),FinanceAccountVo.class));
        page.setTotalCount(new Long(pageInfo.getTotal()).intValue());
        return page;
    }
}
