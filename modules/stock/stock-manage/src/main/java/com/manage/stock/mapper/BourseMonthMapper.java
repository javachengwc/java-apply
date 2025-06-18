package com.manage.stock.mapper;

import java.util.List;
import com.manage.stock.domain.BourseMonth;

/**
 * 证券指数月数据Mapper接口
 * 
 * @author gener
 * @date 2025-06-18
 */
public interface BourseMonthMapper 
{
    /**
     * 查询证券指数月数据
     * 
     * @param id 证券指数月数据主键
     * @return 证券指数月数据
     */
    public BourseMonth selectBourseMonthById(Long id);

    /**
     * 查询证券指数月数据列表
     * 
     * @param bourseMonth 证券指数月数据
     * @return 证券指数月数据集合
     */
    public List<BourseMonth> selectBourseMonthList(BourseMonth bourseMonth);

    /**
     * 新增证券指数月数据
     * 
     * @param bourseMonth 证券指数月数据
     * @return 结果
     */
    public int insertBourseMonth(BourseMonth bourseMonth);

    /**
     * 修改证券指数月数据
     * 
     * @param bourseMonth 证券指数月数据
     * @return 结果
     */
    public int updateBourseMonth(BourseMonth bourseMonth);

    /**
     * 删除证券指数月数据
     * 
     * @param id 证券指数月数据主键
     * @return 结果
     */
    public int deleteBourseMonthById(Long id);

    /**
     * 批量删除证券指数月数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBourseMonthByIds(Long[] ids);
}
