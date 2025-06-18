package com.manage.stock.service;

import java.util.List;
import com.manage.stock.domain.BourseDay;

/**
 * 证券指数天数据Service接口
 * 
 * @author gener
 * @date 2025-06-18
 */
public interface IBourseDayService 
{
    /**
     * 查询证券指数天数据
     * 
     * @param id 证券指数天数据主键
     * @return 证券指数天数据
     */
    public BourseDay selectBourseDayById(Long id);

    /**
     * 查询证券指数天数据列表
     * 
     * @param bourseDay 证券指数天数据
     * @return 证券指数天数据集合
     */
    public List<BourseDay> selectBourseDayList(BourseDay bourseDay);

    /**
     * 新增证券指数天数据
     * 
     * @param bourseDay 证券指数天数据
     * @return 结果
     */
    public int insertBourseDay(BourseDay bourseDay);

    /**
     * 修改证券指数天数据
     * 
     * @param bourseDay 证券指数天数据
     * @return 结果
     */
    public int updateBourseDay(BourseDay bourseDay);

    /**
     * 批量删除证券指数天数据
     * 
     * @param ids 需要删除的证券指数天数据主键集合
     * @return 结果
     */
    public int deleteBourseDayByIds(Long[] ids);

    /**
     * 删除证券指数天数据信息
     * 
     * @param id 证券指数天数据主键
     * @return 结果
     */
    public int deleteBourseDayById(Long id);
}
