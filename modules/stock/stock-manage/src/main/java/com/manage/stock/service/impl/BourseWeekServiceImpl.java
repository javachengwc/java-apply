package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.BourseWeekMapper;
import com.manage.stock.domain.BourseWeek;
import com.manage.stock.service.IBourseWeekService;

/**
 * 证券指数周数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-18
 */
@Service
public class BourseWeekServiceImpl implements IBourseWeekService 
{
    @Autowired
    private BourseWeekMapper bourseWeekMapper;

    /**
     * 查询证券指数周数据
     * 
     * @param id 证券指数周数据主键
     * @return 证券指数周数据
     */
    @Override
    public BourseWeek selectBourseWeekById(Long id)
    {
        return bourseWeekMapper.selectBourseWeekById(id);
    }

    /**
     * 查询证券指数周数据列表
     * 
     * @param bourseWeek 证券指数周数据
     * @return 证券指数周数据
     */
    @Override
    public List<BourseWeek> selectBourseWeekList(BourseWeek bourseWeek)
    {
        return bourseWeekMapper.selectBourseWeekList(bourseWeek);
    }

    /**
     * 新增证券指数周数据
     * 
     * @param bourseWeek 证券指数周数据
     * @return 结果
     */
    @Override
    public int insertBourseWeek(BourseWeek bourseWeek)
    {
        bourseWeek.setCreateTime(DateUtils.getNowDate());
        return bourseWeekMapper.insertBourseWeek(bourseWeek);
    }

    /**
     * 修改证券指数周数据
     * 
     * @param bourseWeek 证券指数周数据
     * @return 结果
     */
    @Override
    public int updateBourseWeek(BourseWeek bourseWeek)
    {
        return bourseWeekMapper.updateBourseWeek(bourseWeek);
    }

    /**
     * 批量删除证券指数周数据
     * 
     * @param ids 需要删除的证券指数周数据主键
     * @return 结果
     */
    @Override
    public int deleteBourseWeekByIds(Long[] ids)
    {
        return bourseWeekMapper.deleteBourseWeekByIds(ids);
    }

    /**
     * 删除证券指数周数据信息
     * 
     * @param id 证券指数周数据主键
     * @return 结果
     */
    @Override
    public int deleteBourseWeekById(Long id)
    {
        return bourseWeekMapper.deleteBourseWeekById(id);
    }
}
