package com.otd.boot.finance.service;

import com.otd.boot.finance.model.entity.FinanceAccount;
import com.otd.boot.finance.model.vo.FinanceAccountSearch;

import java.util.List;

public interface FinanceAccountService  {

    List<FinanceAccount> queryFinanceAccount(FinanceAccountSearch search);
}
