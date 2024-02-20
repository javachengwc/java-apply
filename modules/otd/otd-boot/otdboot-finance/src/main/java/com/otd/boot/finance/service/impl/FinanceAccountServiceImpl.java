package com.otd.boot.finance.service.impl;

import com.otd.boot.finance.dao.FinanceAccountDao;
import com.otd.boot.finance.model.entity.FinanceAccount;
import com.otd.boot.finance.model.vo.FinanceAccountSearch;
import com.otd.boot.finance.service.FinanceAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    private FinanceAccountDao financeAccountDao;

    public List<FinanceAccount> queryFinanceAccount(FinanceAccountSearch search) {
        log.info("FinanceAccountServiceImpl queryFinanceAccount start...");
        List<FinanceAccount> list = financeAccountDao.queryFinanceAccount(search);
        return list;
    }
}
