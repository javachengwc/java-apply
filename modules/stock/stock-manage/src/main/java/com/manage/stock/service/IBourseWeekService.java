package com.manage.stock.service;

import java.util.List;
import com.manage.stock.domain.BourseWeek;

/**
 * 证券指数周数据Service接口
 * 
 * @author gener
 * @date 2025-06-18
 */
public interface IBourseWeekService 
{
    /**
     * 查询证券指数周数据
     * 
     * @param id 证券指数周数据主键
     * @return 证券指数周数据
     */
    public BourseWeek selectBourseWeekById(Long id);

    /**
     * 查询证券指数周数据列表
     * 
     * @param bourseWeek 证券指数周数据
     * @return 证券指数周数据集合
     */
    public List<BourseWeek> selectBourseWeekList(BourseWeek bourseWeek);

    /**
     * 新增证券指数周数据
     * 
     * @param bourseWeek 证券指数周数据
     * @return 结果
     */
    public int insertBourseWeek(BourseWeek bourseWeek);

    /**
     * 修改证券指数周数据
     * 
     * @param bourseWeek 证券指数周数据
     * @return 结果
     */
    public int updateBourseWeek(BourseWeek bourseWeek);

    /**
     * 批量删除证券指数周数据
     * 
     * @param ids 需要删除的证券指数周数据主键集合
     * @return 结果
     */
    public int deleteBourseWeekByIds(Long[] ids);

    /**
     * 删除证券指数周数据信息
     * 
     * @param id 证券指数周数据主键
     * @return 结果
     */
    public int deleteBourseWeekById(Long id);
}
