package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.BourseDayMapper;
import com.manage.stock.domain.BourseDay;
import com.manage.stock.service.IBourseDayService;

/**
 * 证券指数天数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-18
 */
@Service
public class BourseDayServiceImpl implements IBourseDayService 
{
    @Autowired
    private BourseDayMapper bourseDayMapper;

    /**
     * 查询证券指数天数据
     * 
     * @param id 证券指数天数据主键
     * @return 证券指数天数据
     */
    @Override
    public BourseDay selectBourseDayById(Long id)
    {
        return bourseDayMapper.selectBourseDayById(id);
    }

    /**
     * 查询证券指数天数据列表
     * 
     * @param bourseDay 证券指数天数据
     * @return 证券指数天数据
     */
    @Override
    public List<BourseDay> selectBourseDayList(BourseDay bourseDay)
    {
        return bourseDayMapper.selectBourseDayList(bourseDay);
    }

    /**
     * 新增证券指数天数据
     * 
     * @param bourseDay 证券指数天数据
     * @return 结果
     */
    @Override
    public int insertBourseDay(BourseDay bourseDay)
    {
        bourseDay.setCreateTime(DateUtils.getNowDate());
        return bourseDayMapper.insertBourseDay(bourseDay);
    }

    /**
     * 修改证券指数天数据
     * 
     * @param bourseDay 证券指数天数据
     * @return 结果
     */
    @Override
    public int updateBourseDay(BourseDay bourseDay)
    {
        return bourseDayMapper.updateBourseDay(bourseDay);
    }

    /**
     * 批量删除证券指数天数据
     * 
     * @param ids 需要删除的证券指数天数据主键
     * @return 结果
     */
    @Override
    public int deleteBourseDayByIds(Long[] ids)
    {
        return bourseDayMapper.deleteBourseDayByIds(ids);
    }

    /**
     * 删除证券指数天数据信息
     * 
     * @param id 证券指数天数据主键
     * @return 结果
     */
    @Override
    public int deleteBourseDayById(Long id)
    {
        return bourseDayMapper.deleteBourseDayById(id);
    }
}
