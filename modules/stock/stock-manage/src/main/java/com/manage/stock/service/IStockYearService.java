package com.manage.stock.service;

import java.util.List;
import com.manage.stock.domain.StockYear;

/**
 * 股票年数据Service接口
 * 
 * @author gener
 * @date 2025-06-17
 */
public interface IStockYearService 
{
    /**
     * 查询股票年数据
     * 
     * @param id 股票年数据主键
     * @return 股票年数据
     */
    public StockYear selectStockYearById(Long id);

    /**
     * 查询股票年数据列表
     * 
     * @param stockYear 股票年数据
     * @return 股票年数据集合
     */
    public List<StockYear> selectStockYearList(StockYear stockYear);

    /**
     * 新增股票年数据
     * 
     * @param stockYear 股票年数据
     * @return 结果
     */
    public int insertStockYear(StockYear stockYear);

    /**
     * 修改股票年数据
     * 
     * @param stockYear 股票年数据
     * @return 结果
     */
    public int updateStockYear(StockYear stockYear);

    /**
     * 批量删除股票年数据
     * 
     * @param ids 需要删除的股票年数据主键集合
     * @return 结果
     */
    public int deleteStockYearByIds(Long[] ids);

    /**
     * 删除股票年数据信息
     * 
     * @param id 股票年数据主键
     * @return 结果
     */
    public int deleteStockYearById(Long id);
}
