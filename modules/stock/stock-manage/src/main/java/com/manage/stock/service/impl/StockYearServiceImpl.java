package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.StockYearMapper;
import com.manage.stock.domain.StockYear;
import com.manage.stock.service.IStockYearService;

/**
 * 股票年数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-17
 */
@Service
public class StockYearServiceImpl implements IStockYearService 
{
    @Autowired
    private StockYearMapper stockYearMapper;

    /**
     * 查询股票年数据
     * 
     * @param id 股票年数据主键
     * @return 股票年数据
     */
    @Override
    public StockYear selectStockYearById(Long id)
    {
        return stockYearMapper.selectStockYearById(id);
    }

    /**
     * 查询股票年数据列表
     * 
     * @param stockYear 股票年数据
     * @return 股票年数据
     */
    @Override
    public List<StockYear> selectStockYearList(StockYear stockYear)
    {
        return stockYearMapper.selectStockYearList(stockYear);
    }

    /**
     * 新增股票年数据
     * 
     * @param stockYear 股票年数据
     * @return 结果
     */
    @Override
    public int insertStockYear(StockYear stockYear)
    {
        stockYear.setCreateTime(DateUtils.getNowDate());
        return stockYearMapper.insertStockYear(stockYear);
    }

    /**
     * 修改股票年数据
     * 
     * @param stockYear 股票年数据
     * @return 结果
     */
    @Override
    public int updateStockYear(StockYear stockYear)
    {
        return stockYearMapper.updateStockYear(stockYear);
    }

    /**
     * 批量删除股票年数据
     * 
     * @param ids 需要删除的股票年数据主键
     * @return 结果
     */
    @Override
    public int deleteStockYearByIds(Long[] ids)
    {
        return stockYearMapper.deleteStockYearByIds(ids);
    }

    /**
     * 删除股票年数据信息
     * 
     * @param id 股票年数据主键
     * @return 结果
     */
    @Override
    public int deleteStockYearById(Long id)
    {
        return stockYearMapper.deleteStockYearById(id);
    }
}
