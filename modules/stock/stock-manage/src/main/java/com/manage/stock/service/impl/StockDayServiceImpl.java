package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.StockDayMapper;
import com.manage.stock.domain.StockDay;
import com.manage.stock.service.IStockDayService;

/**
 * 股票天数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-18
 */
@Service
public class StockDayServiceImpl implements IStockDayService 
{
    @Autowired
    private StockDayMapper stockDayMapper;

    /**
     * 查询股票天数据
     * 
     * @param id 股票天数据主键
     * @return 股票天数据
     */
    @Override
    public StockDay selectStockDayById(Long id)
    {
        return stockDayMapper.selectStockDayById(id);
    }

    /**
     * 查询股票天数据列表
     * 
     * @param stockDay 股票天数据
     * @return 股票天数据
     */
    @Override
    public List<StockDay> selectStockDayList(StockDay stockDay)
    {
        return stockDayMapper.selectStockDayList(stockDay);
    }

    /**
     * 新增股票天数据
     * 
     * @param stockDay 股票天数据
     * @return 结果
     */
    @Override
    public int insertStockDay(StockDay stockDay)
    {
        stockDay.setCreateTime(DateUtils.getNowDate());
        return stockDayMapper.insertStockDay(stockDay);
    }

    /**
     * 修改股票天数据
     * 
     * @param stockDay 股票天数据
     * @return 结果
     */
    @Override
    public int updateStockDay(StockDay stockDay)
    {
        return stockDayMapper.updateStockDay(stockDay);
    }

    /**
     * 批量删除股票天数据
     * 
     * @param ids 需要删除的股票天数据主键
     * @return 结果
     */
    @Override
    public int deleteStockDayByIds(Long[] ids)
    {
        return stockDayMapper.deleteStockDayByIds(ids);
    }

    /**
     * 删除股票天数据信息
     * 
     * @param id 股票天数据主键
     * @return 结果
     */
    @Override
    public int deleteStockDayById(Long id)
    {
        return stockDayMapper.deleteStockDayById(id);
    }
}
