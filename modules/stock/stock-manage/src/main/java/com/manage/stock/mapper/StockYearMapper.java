package com.manage.stock.mapper;

import java.util.List;
import com.manage.stock.domain.StockYear;
import com.manage.stock.domain.dto.StockQueryParam;

/**
 * 股票年数据Mapper接口
 * 
 * @author gener
 * @date 2025-06-17
 */
public interface StockYearMapper 
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
     * @param queryParam 股票年查询条件
     * @return 股票年数据集合
     */
    public List<StockYear> selectStockYearList(StockQueryParam queryParam);

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
     * 删除股票年数据
     * 
     * @param id 股票年数据主键
     * @return 结果
     */
    public int deleteStockYearById(Long id);

    /**
     * 批量删除股票年数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockYearByIds(Long[] ids);
}
