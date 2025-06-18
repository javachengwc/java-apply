package com.manage.stock.mapper;

import java.util.List;
import com.manage.stock.domain.StockMonth;

/**
 * 股票月数据Mapper接口
 * 
 * @author gener
 * @date 2025-06-18
 */
public interface StockMonthMapper 
{
    /**
     * 查询股票月数据
     * 
     * @param id 股票月数据主键
     * @return 股票月数据
     */
    public StockMonth selectStockMonthById(Long id);

    /**
     * 查询股票月数据列表
     * 
     * @param stockMonth 股票月数据
     * @return 股票月数据集合
     */
    public List<StockMonth> selectStockMonthList(StockMonth stockMonth);

    /**
     * 新增股票月数据
     * 
     * @param stockMonth 股票月数据
     * @return 结果
     */
    public int insertStockMonth(StockMonth stockMonth);

    /**
     * 修改股票月数据
     * 
     * @param stockMonth 股票月数据
     * @return 结果
     */
    public int updateStockMonth(StockMonth stockMonth);

    /**
     * 删除股票月数据
     * 
     * @param id 股票月数据主键
     * @return 结果
     */
    public int deleteStockMonthById(Long id);

    /**
     * 批量删除股票月数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockMonthByIds(Long[] ids);
}
