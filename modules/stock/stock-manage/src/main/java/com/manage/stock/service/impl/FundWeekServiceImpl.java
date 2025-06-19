package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.FundWeekMapper;
import com.manage.stock.domain.FundWeek;
import com.manage.stock.service.IFundWeekService;

/**
 * 基金周数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-19
 */
@Service
public class FundWeekServiceImpl implements IFundWeekService 
{
    @Autowired
    private FundWeekMapper fundWeekMapper;

    /**
     * 查询基金周数据
     * 
     * @param id 基金周数据主键
     * @return 基金周数据
     */
    @Override
    public FundWeek selectFundWeekById(Long id)
    {
        return fundWeekMapper.selectFundWeekById(id);
    }

    /**
     * 查询基金周数据列表
     * 
     * @param fundWeek 基金周数据
     * @return 基金周数据
     */
    @Override
    public List<FundWeek> selectFundWeekList(FundWeek fundWeek)
    {
        return fundWeekMapper.selectFundWeekList(fundWeek);
    }

    /**
     * 新增基金周数据
     * 
     * @param fundWeek 基金周数据
     * @return 结果
     */
    @Override
    public int insertFundWeek(FundWeek fundWeek)
    {
        fundWeek.setCreateTime(DateUtils.getNowDate());
        return fundWeekMapper.insertFundWeek(fundWeek);
    }

    /**
     * 修改基金周数据
     * 
     * @param fundWeek 基金周数据
     * @return 结果
     */
    @Override
    public int updateFundWeek(FundWeek fundWeek)
    {
        return fundWeekMapper.updateFundWeek(fundWeek);
    }

    /**
     * 批量删除基金周数据
     * 
     * @param ids 需要删除的基金周数据主键
     * @return 结果
     */
    @Override
    public int deleteFundWeekByIds(Long[] ids)
    {
        return fundWeekMapper.deleteFundWeekByIds(ids);
    }

    /**
     * 删除基金周数据信息
     * 
     * @param id 基金周数据主键
     * @return 结果
     */
    @Override
    public int deleteFundWeekById(Long id)
    {
        return fundWeekMapper.deleteFundWeekById(id);
    }
}
