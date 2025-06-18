package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.BourseMonthMapper;
import com.manage.stock.domain.BourseMonth;
import com.manage.stock.service.IBourseMonthService;

/**
 * 证券指数月数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-18
 */
@Service
public class BourseMonthServiceImpl implements IBourseMonthService 
{
    @Autowired
    private BourseMonthMapper bourseMonthMapper;

    /**
     * 查询证券指数月数据
     * 
     * @param id 证券指数月数据主键
     * @return 证券指数月数据
     */
    @Override
    public BourseMonth selectBourseMonthById(Long id)
    {
        return bourseMonthMapper.selectBourseMonthById(id);
    }

    /**
     * 查询证券指数月数据列表
     * 
     * @param bourseMonth 证券指数月数据
     * @return 证券指数月数据
     */
    @Override
    public List<BourseMonth> selectBourseMonthList(BourseMonth bourseMonth)
    {
        return bourseMonthMapper.selectBourseMonthList(bourseMonth);
    }

    /**
     * 新增证券指数月数据
     * 
     * @param bourseMonth 证券指数月数据
     * @return 结果
     */
    @Override
    public int insertBourseMonth(BourseMonth bourseMonth)
    {
        bourseMonth.setCreateTime(DateUtils.getNowDate());
        return bourseMonthMapper.insertBourseMonth(bourseMonth);
    }

    /**
     * 修改证券指数月数据
     * 
     * @param bourseMonth 证券指数月数据
     * @return 结果
     */
    @Override
    public int updateBourseMonth(BourseMonth bourseMonth)
    {
        return bourseMonthMapper.updateBourseMonth(bourseMonth);
    }

    /**
     * 批量删除证券指数月数据
     * 
     * @param ids 需要删除的证券指数月数据主键
     * @return 结果
     */
    @Override
    public int deleteBourseMonthByIds(Long[] ids)
    {
        return bourseMonthMapper.deleteBourseMonthByIds(ids);
    }

    /**
     * 删除证券指数月数据信息
     * 
     * @param id 证券指数月数据主键
     * @return 结果
     */
    @Override
    public int deleteBourseMonthById(Long id)
    {
        return bourseMonthMapper.deleteBourseMonthById(id);
    }
}
