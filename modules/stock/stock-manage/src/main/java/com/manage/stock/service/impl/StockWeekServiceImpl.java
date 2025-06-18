package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.StockWeekMapper;
import com.manage.stock.domain.StockWeek;
import com.manage.stock.service.IStockWeekService;

/**
 * 股票周数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-18
 */
@Service
public class StockWeekServiceImpl implements IStockWeekService 
{
    @Autowired
    private StockWeekMapper stockWeekMapper;

    /**
     * 查询股票周数据
     * 
     * @param id 股票周数据主键
     * @return 股票周数据
     */
    @Override
    public StockWeek selectStockWeekById(Long id)
    {
        return stockWeekMapper.selectStockWeekById(id);
    }

    /**
     * 查询股票周数据列表
     * 
     * @param stockWeek 股票周数据
     * @return 股票周数据
     */
    @Override
    public List<StockWeek> selectStockWeekList(StockWeek stockWeek)
    {
        return stockWeekMapper.selectStockWeekList(stockWeek);
    }

    /**
     * 新增股票周数据
     * 
     * @param stockWeek 股票周数据
     * @return 结果
     */
    @Override
    public int insertStockWeek(StockWeek stockWeek)
    {
        stockWeek.setCreateTime(DateUtils.getNowDate());
        return stockWeekMapper.insertStockWeek(stockWeek);
    }

    /**
     * 修改股票周数据
     * 
     * @param stockWeek 股票周数据
     * @return 结果
     */
    @Override
    public int updateStockWeek(StockWeek stockWeek)
    {
        return stockWeekMapper.updateStockWeek(stockWeek);
    }

    /**
     * 批量删除股票周数据
     * 
     * @param ids 需要删除的股票周数据主键
     * @return 结果
     */
    @Override
    public int deleteStockWeekByIds(Long[] ids)
    {
        return stockWeekMapper.deleteStockWeekByIds(ids);
    }

    /**
     * 删除股票周数据信息
     * 
     * @param id 股票周数据主键
     * @return 结果
     */
    @Override
    public int deleteStockWeekById(Long id)
    {
        return stockWeekMapper.deleteStockWeekById(id);
    }
}
