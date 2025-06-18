package com.manage.stock.mapper;

import java.util.List;
import com.manage.stock.domain.StockDay;

/**
 * 股票天数据Mapper接口
 * 
 * @author gener
 * @date 2025-06-18
 */
public interface StockDayMapper 
{
    /**
     * 查询股票天数据
     * 
     * @param id 股票天数据主键
     * @return 股票天数据
     */
    public StockDay selectStockDayById(Long id);

    /**
     * 查询股票天数据列表
     * 
     * @param stockDay 股票天数据
     * @return 股票天数据集合
     */
    public List<StockDay> selectStockDayList(StockDay stockDay);

    /**
     * 新增股票天数据
     * 
     * @param stockDay 股票天数据
     * @return 结果
     */
    public int insertStockDay(StockDay stockDay);

    /**
     * 修改股票天数据
     * 
     * @param stockDay 股票天数据
     * @return 结果
     */
    public int updateStockDay(StockDay stockDay);

    /**
     * 删除股票天数据
     * 
     * @param id 股票天数据主键
     * @return 结果
     */
    public int deleteStockDayById(Long id);

    /**
     * 批量删除股票天数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockDayByIds(Long[] ids);
}
