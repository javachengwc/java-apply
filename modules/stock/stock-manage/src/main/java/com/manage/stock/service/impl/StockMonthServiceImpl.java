package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.StockMonthMapper;
import com.manage.stock.domain.StockMonth;
import com.manage.stock.service.IStockMonthService;

/**
 * 股票月数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-18
 */
@Service
public class StockMonthServiceImpl implements IStockMonthService 
{
    @Autowired
    private StockMonthMapper stockMonthMapper;

    /**
     * 查询股票月数据
     * 
     * @param id 股票月数据主键
     * @return 股票月数据
     */
    @Override
    public StockMonth selectStockMonthById(Long id)
    {
        return stockMonthMapper.selectStockMonthById(id);
    }

    /**
     * 查询股票月数据列表
     * 
     * @param stockMonth 股票月数据
     * @return 股票月数据
     */
    @Override
    public List<StockMonth> selectStockMonthList(StockMonth stockMonth)
    {
        return stockMonthMapper.selectStockMonthList(stockMonth);
    }

    /**
     * 新增股票月数据
     * 
     * @param stockMonth 股票月数据
     * @return 结果
     */
    @Override
    public int insertStockMonth(StockMonth stockMonth)
    {
        stockMonth.setCreateTime(DateUtils.getNowDate());
        return stockMonthMapper.insertStockMonth(stockMonth);
    }

    /**
     * 修改股票月数据
     * 
     * @param stockMonth 股票月数据
     * @return 结果
     */
    @Override
    public int updateStockMonth(StockMonth stockMonth)
    {
        return stockMonthMapper.updateStockMonth(stockMonth);
    }

    /**
     * 批量删除股票月数据
     * 
     * @param ids 需要删除的股票月数据主键
     * @return 结果
     */
    @Override
    public int deleteStockMonthByIds(Long[] ids)
    {
        return stockMonthMapper.deleteStockMonthByIds(ids);
    }

    /**
     * 删除股票月数据信息
     * 
     * @param id 股票月数据主键
     * @return 结果
     */
    @Override
    public int deleteStockMonthById(Long id)
    {
        return stockMonthMapper.deleteStockMonthById(id);
    }
}
