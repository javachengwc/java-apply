package com.otd.boot.finance.dao;

import com.otd.boot.finance.model.entity.FinanceAccount;
import com.otd.boot.finance.model.vo.FinanceAccountSearch;

import java.util.List;

public interface FinanceAccountDao {

    List<FinanceAccount> queryFinanceAccount(FinanceAccountSearch search);
}
