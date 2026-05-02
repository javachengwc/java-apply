package com.manage.stock.service.impl;

import java.util.Date;
import java.util.List;

import com.manage.stock.domain.dto.StockQueryParam;
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
     * @param queryParam 股票天查询参数
     * @return 股票天数据
     */
    @Override
    public List<StockDay> selectStockDayList(StockQueryParam queryParam)
    {
        return stockDayMapper.selectStockDayList(queryParam);
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
        Date now = new Date();
        stockDay.setCreateTime(now);
        stockDay.setUpdateTime(now);
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
        stockDay.setUpdateTime(new Date());
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
