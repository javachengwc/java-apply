package com.manage.stock.mapper;

import java.util.List;
import com.manage.stock.domain.StockWeek;

/**
 * 股票周数据Mapper接口
 * 
 * @author gener
 * @date 2025-06-18
 */
public interface StockWeekMapper 
{
    /**
     * 查询股票周数据
     * 
     * @param id 股票周数据主键
     * @return 股票周数据
     */
    public StockWeek selectStockWeekById(Long id);

    /**
     * 查询股票周数据列表
     * 
     * @param stockWeek 股票周数据
     * @return 股票周数据集合
     */
    public List<StockWeek> selectStockWeekList(StockWeek stockWeek);

    /**
     * 新增股票周数据
     * 
     * @param stockWeek 股票周数据
     * @return 结果
     */
    public int insertStockWeek(StockWeek stockWeek);

    /**
     * 修改股票周数据
     * 
     * @param stockWeek 股票周数据
     * @return 结果
     */
    public int updateStockWeek(StockWeek stockWeek);

    /**
     * 删除股票周数据
     * 
     * @param id 股票周数据主键
     * @return 结果
     */
    public int deleteStockWeekById(Long id);

    /**
     * 批量删除股票周数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockWeekByIds(Long[] ids);
}
